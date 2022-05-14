package dev.mslalith.focuslauncher.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.data.model.AppDrawerViewType
import dev.mslalith.focuslauncher.data.model.ClockAlignment
import dev.mslalith.focuslauncher.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.QuotesSettingsRepo
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_DRAWER_VIEW_TYPE
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_GROUP_HEADER
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_APP_ICONS
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.AppDrawer.DEFAULT_SEARCH_BAR
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_24_ANIMATION_DURATION
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_CLOCK_ALIGNMENT
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Clock.DEFAULT_SHOW_CLOCK_24
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_FIRST_RUN
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.General.DEFAULT_STATUS_BAR
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_ILLUMINATION_PERCENT
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_LUNAR_PHASE
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.LunarPhase.DEFAULT_SHOW_UPCOMING_PHASE_DETAILS
import dev.mslalith.focuslauncher.data.utils.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appDrawerSettingsRepo: AppDrawerSettingsRepo,
    private val clockSettingsRepo: ClockSettingsRepo,
    private val lunarPhaseSettingsRepo: LunarPhaseSettingsRepo,
    private val quotesSettingsRepo: QuotesSettingsRepo,
) : ViewModel() {

    /**
     * General Settings
     */
    val firstRunStateFlow = generalSettingsRepo.firstRunFlow.withinScope(DEFAULT_FIRST_RUN)
    val statusBarVisibilityStateFlow = generalSettingsRepo.statusBarVisibilityFlow.withinScope(DEFAULT_STATUS_BAR)
    val notificationShadeStateFlow = generalSettingsRepo.notificationShadeFlow.withinScope(DEFAULT_NOTIFICATION_SHADE)

    fun overrideFirstRun() { launch { generalSettingsRepo.overrideFirstRun() } }
    fun toggleStatusBarVisibility() { launch { generalSettingsRepo.toggleStatusBarVisibility() } }
    fun toggleNotificationShade() { launch { generalSettingsRepo.toggleNotificationShade() } }

    /**
     * App Drawer Settings
     */
    val appDrawerViewTypeStateFlow = appDrawerSettingsRepo.appDrawerViewTypeFlow.withinScope(DEFAULT_APP_DRAWER_VIEW_TYPE)
    val appIconsVisibilityStateFlow = appDrawerSettingsRepo.appIconsVisibilityFlow.withinScope(DEFAULT_APP_ICONS)
    val searchBarVisibilityStateFlow = appDrawerSettingsRepo.searchBarVisibilityFlow.withinScope(DEFAULT_SEARCH_BAR)
    val appGroupHeaderVisibilityStateFlow = appDrawerSettingsRepo.appGroupHeaderVisibilityFlow.withinScope(DEFAULT_APP_GROUP_HEADER)

    fun updateAppDrawerViewType(appDrawerViewType: AppDrawerViewType) { launch { appDrawerSettingsRepo.updateAppDrawerViewType(appDrawerViewType) } }
    fun toggleAppIconsVisibility() { launch { appDrawerSettingsRepo.toggleAppIconsVisibility() } }
    fun toggleSearchBarVisibility() { launch { appDrawerSettingsRepo.toggleSearchBarVisibility() } }
    fun toggleAppGroupHeaderVisibility() { launch { appDrawerSettingsRepo.toggleAppGroupHeaderVisibility() } }

    /**
     * Clock Settings
     */
    val showClock24StateFlow = clockSettingsRepo.showClock24Flow.withinScope(DEFAULT_SHOW_CLOCK_24)
    val clockAlignmentStateFlow = clockSettingsRepo.clockAlignmentFlow.withinScope(DEFAULT_CLOCK_ALIGNMENT)
    val clock24AnimationDurationStateFlow = clockSettingsRepo.clock24AnimationDurationFlow.withinScope(DEFAULT_CLOCK_24_ANIMATION_DURATION)

    fun toggleClock24() { launch { clockSettingsRepo.toggleClock24() } }
    fun updateClockAlignment(clockAlignment: ClockAlignment) { launch { clockSettingsRepo.updateClockAlignment(clockAlignment) } }
    fun updateClock24AnimationDuration(duration: Int) { launch { clockSettingsRepo.updateClock24AnimationDuration(duration) } }

    /**
     * Lunar Phase Settings
     */
    val showLunarPhaseStateFlow = lunarPhaseSettingsRepo.showLunarPhaseFlow.withinScope(DEFAULT_SHOW_LUNAR_PHASE)
    val showIlluminationPercentStateFlow = lunarPhaseSettingsRepo.showIlluminationPercentFlow.withinScope(DEFAULT_SHOW_ILLUMINATION_PERCENT)
    val showUpcomingPhaseDetailsStateFlow = lunarPhaseSettingsRepo.showUpcomingPhaseDetailsFlow.withinScope(DEFAULT_SHOW_UPCOMING_PHASE_DETAILS)

    fun toggleShowLunarPhase() { launch { lunarPhaseSettingsRepo.toggleShowLunarPhase() } }
    fun toggleShowIlluminationPercent() { launch { lunarPhaseSettingsRepo.toggleShowIlluminationPercent() } }
    fun toggleShowUpcomingPhaseDetails() { launch { lunarPhaseSettingsRepo.toggleShowUpcomingPhaseDetails() } }

    /**
     * Quotes Settings
     */
    val showQuotesStateFlow = quotesSettingsRepo.showQuotesFlow.withinScope(DEFAULT_SHOW_QUOTES)
    fun toggleShowQuotes() { launch { quotesSettingsRepo.toggleShowQuotes() } }

    private fun launch(
        coroutineContext: CoroutineContext = Dispatchers.IO,
        run: suspend () -> Unit,
    ) = viewModelScope.launch(coroutineContext) { run() }

    private fun <T> Flow<T>.withinScope(
        initialValue: T
    ) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue
    )
}
