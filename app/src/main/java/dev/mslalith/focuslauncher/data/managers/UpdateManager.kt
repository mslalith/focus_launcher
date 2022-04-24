package dev.mslalith.focuslauncher.data.managers

import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallErrorCode
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dev.mslalith.focuslauncher.data.managers.AppUpdateState.CheckForUpdates
import dev.mslalith.focuslauncher.data.managers.AppUpdateState.CheckingForUpdates
import dev.mslalith.focuslauncher.data.managers.AppUpdateState.DownloadFailed
import dev.mslalith.focuslauncher.data.managers.AppUpdateState.Downloaded
import dev.mslalith.focuslauncher.data.managers.AppUpdateState.Downloading
import dev.mslalith.focuslauncher.data.managers.AppUpdateState.Installing
import dev.mslalith.focuslauncher.data.managers.AppUpdateState.NoUpdateAvailable
import dev.mslalith.focuslauncher.data.managers.AppUpdateState.TryAgain
import dev.mslalith.focuslauncher.extensions.waitAndRunAfter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class AppUpdateState(open val message: String) {
    object CheckForUpdates : AppUpdateState("Check for updates")
    object CheckingForUpdates : AppUpdateState("Checking for updates...")
    object NoUpdateAvailable : AppUpdateState("No updates available")
    data class Downloading(override val message: String) : AppUpdateState(message)
    object Downloaded : AppUpdateState("Reload to update")
    object DownloadFailed : AppUpdateState("Download failed")
    object Installing : AppUpdateState("Installing...")
    data class TryAgain(override val message: String) : AppUpdateState(message)
}

class UpdateManager @Inject constructor(context: Context) : InstallStateUpdatedListener {
    private var mAppUpdateManager = AppUpdateManagerFactory.create(context)

    private val _appUpdateStateFlow = MutableStateFlow<AppUpdateState>(CheckForUpdates)
    val appUpdateStateFlow = _appUpdateStateFlow.asStateFlow()

    override fun onStateUpdate(state: InstallState) {
        if (state.installErrorCode() == InstallErrorCode.NO_ERROR) {
            when (state.installStatus()) {
                InstallStatus.PENDING -> _appUpdateStateFlow.value = Downloading("Downloading...")
                InstallStatus.DOWNLOADING -> {
                    val percent = state.bytesDownloaded() / state.totalBytesToDownload().toDouble()
                    val percentToShow = (percent * 100).toInt()
                    _appUpdateStateFlow.value = Downloading("Downloading... ($percentToShow%)")
                }
                InstallStatus.DOWNLOADED -> _appUpdateStateFlow.value = Downloaded
                InstallStatus.INSTALLING -> _appUpdateStateFlow.value = Installing
                InstallStatus.INSTALLED -> {
                    resetAppUpdateState()
                    mAppUpdateManager.unregisterListener(this)
                }
                InstallStatus.FAILED, InstallStatus.CANCELED -> _appUpdateStateFlow.value = DownloadFailed
                else -> _appUpdateStateFlow.value = TryAgain("Failed to update, try again (${state.installStatus()})")
            }
        } else {
            val message = getAppropriateMessage(state.installErrorCode())
            if (message.isNotEmpty()) {
                _appUpdateStateFlow.value = TryAgain(message)
            }
        }
    }

    fun checkForUpdate(activity: Activity) {
        _appUpdateStateFlow.value = CheckingForUpdates
        val startTime = System.currentTimeMillis()

        mAppUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                waitAndRunAfter(startTime = startTime) {
                    val isFlexibleUpdateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                    val isDownloaded = appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED
                    when {
                        isFlexibleUpdateAvailable -> initiateDownload(activity = activity, appUpdateInfo = appUpdateInfo)
                        isDownloaded -> _appUpdateStateFlow.value = Downloaded
                        else -> _appUpdateStateFlow.value = NoUpdateAvailable
                    }
                }
            }
    }

    private fun initiateDownload(activity: Activity, appUpdateInfo: AppUpdateInfo) {
        val appUpdateOptions = AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
        mAppUpdateManager.registerListener(this)
        mAppUpdateManager.startUpdateFlow(appUpdateInfo, activity, appUpdateOptions)
            .addOnCompleteListener {
                if (it.result == 0) {
                    _appUpdateStateFlow.value = TryAgain("Update cancelled")
                }
            }
    }

    fun completeUpdate() {
        mAppUpdateManager.completeUpdate()
    }

    fun resetAppUpdateState() {
        _appUpdateStateFlow.value = CheckForUpdates
    }

    private fun getAppropriateMessage(@InstallErrorCode installErrorCode: Int): String {
        return when (installErrorCode) {
            InstallErrorCode.ERROR_API_NOT_AVAILABLE -> "Device doesn't support this feature"
            InstallErrorCode.ERROR_APP_NOT_OWNED -> "App is not owned by any user on this device"
            InstallErrorCode.ERROR_DOWNLOAD_NOT_PRESENT -> "Update has not been fully downloaded yet"
            InstallErrorCode.ERROR_INSTALL_NOT_ALLOWED -> "Install is not allowed, due to the current device state (e.g. low battery, low disk space, ...)"
            InstallErrorCode.ERROR_INTERNAL_ERROR -> "An internal error happened in Play Store"
            InstallErrorCode.ERROR_INVALID_REQUEST -> "Malformed request"
            InstallErrorCode.ERROR_PLAY_STORE_NOT_FOUND -> "Play Store app is either not installed or not the official version"
            InstallErrorCode.ERROR_UNKNOWN -> "An unknown error occurred"
            else -> ""
        }
    }
}
