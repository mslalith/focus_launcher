package dev.mslalith.focuslauncher.screens.launcher

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import javax.inject.Inject

@HiltViewModel
internal class LauncherViewModel @Inject constructor(
    private val launcherAppsManager: LauncherAppsManager,
    private val appDrawerRepo: AppDrawerRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    fun loadApps(forceLoad: Boolean = false) {
        appCoroutineDispatcher.launchInIO {
            appDrawerRepo.apply {
                if (!forceLoad && !areAppsEmptyInDatabase()) return@launchInIO
                addApps(apps = launcherAppsManager.loadAllApps())
            }
        }
    }
}
