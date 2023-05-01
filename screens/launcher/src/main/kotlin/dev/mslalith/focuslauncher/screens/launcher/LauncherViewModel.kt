package dev.mslalith.focuslauncher.screens.launcher

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.domain.launcherapps.LoadAllAppsUseCase
import dev.mslalith.focuslauncher.core.ui.extensions.launchInIO
import javax.inject.Inject

@HiltViewModel
internal class LauncherViewModel @Inject constructor(
    private val loadAllAppsUseCase: LoadAllAppsUseCase,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    fun loadApps(forceLoad: Boolean = false) {
        appCoroutineDispatcher.launchInIO {
            loadAllAppsUseCase(forceLoad = forceLoad)
        }
    }
}
