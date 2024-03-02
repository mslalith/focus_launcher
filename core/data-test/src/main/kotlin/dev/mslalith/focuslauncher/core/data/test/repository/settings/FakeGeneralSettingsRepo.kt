package dev.mslalith.focuslauncher.core.data.test.repository.settings

import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_ICON_PACK_TYPE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_IS_DEFAULT_LAUNCHER
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_REPORT_CRASHES
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import dev.mslalith.focuslauncher.core.model.IconPackType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeGeneralSettingsRepo : GeneralSettingsRepo {

    private val firstRunStateFlow = MutableStateFlow(value = DEFAULT_FIRST_RUN)
    override val firstRunFlow: Flow<Boolean> = firstRunStateFlow

    private val statusBarVisibilityStateFlow = MutableStateFlow(value = DEFAULT_STATUS_BAR)
    override val statusBarVisibilityFlow: Flow<Boolean> = statusBarVisibilityStateFlow

    private val notificationShadeStateFlow = MutableStateFlow(value = DEFAULT_NOTIFICATION_SHADE)
    override val notificationShadeFlow: Flow<Boolean> = notificationShadeStateFlow

    private val isDefaultLauncherStateFlow = MutableStateFlow(value = DEFAULT_IS_DEFAULT_LAUNCHER)
    override val isDefaultLauncher: Flow<Boolean> = isDefaultLauncherStateFlow

    private val iconPackTypeStateFlow = MutableStateFlow<IconPackType>(value = DEFAULT_ICON_PACK_TYPE)
    override val iconPackTypeFlow: Flow<IconPackType> = iconPackTypeStateFlow

    private val reportCrashesStateFlow = MutableStateFlow(value = DEFAULT_REPORT_CRASHES)
    override val reportCrashesFlow: Flow<Boolean> = reportCrashesStateFlow

    override suspend fun overrideFirstRun() {
        firstRunStateFlow.update { false }
    }

    override suspend fun toggleStatusBarVisibility() {
        statusBarVisibilityStateFlow.update { !it }
    }

    override suspend fun toggleNotificationShade() {
        notificationShadeStateFlow.update { !it }
    }

    override suspend fun setIsDefaultLauncher(isDefault: Boolean) {
        isDefaultLauncherStateFlow.update { isDefault }
    }

    override suspend fun updateIconPackType(iconPackType: IconPackType) {
        iconPackTypeStateFlow.update { iconPackType }
    }

    override suspend fun updateReportCrashes(enabled: Boolean) {
        reportCrashesStateFlow.update { enabled }
    }
}
