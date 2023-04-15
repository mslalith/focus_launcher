package dev.mslalith.focuslauncher.feature.homepage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.data.utils.Constants.Defaults.Settings.General.DEFAULT_NOTIFICATION_SHADE
import dev.mslalith.focuslauncher.core.ui.extensions.withinScope
import dev.mslalith.focuslauncher.feature.homepage.model.HomePageState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

@HiltViewModel
internal class HomePageViewModel @Inject constructor(
    generalSettingsRepo: GeneralSettingsRepo,
) : ViewModel() {

    private val defaultHomePageState = HomePageState(
        isPullDownNotificationShadeEnabled = DEFAULT_NOTIFICATION_SHADE
    )

    val homePageState = flowOf(defaultHomePageState)
        .combine(generalSettingsRepo.notificationShadeFlow) { state, isPullDownNotificationShadeEnabled ->
            state.copy(isPullDownNotificationShadeEnabled = isPullDownNotificationShadeEnabled)
        }.withinScope(initialValue = defaultHomePageState)

    private val _showMoonCalendarDetailsDialogStateFlow = MutableStateFlow(false)
    val showMoonCalendarDetailsDialogStateFlow: StateFlow<Boolean>
        get() = _showMoonCalendarDetailsDialogStateFlow

    fun showMoonCalendarDetailsDialog() {
        _showMoonCalendarDetailsDialogStateFlow.value = true
    }

    fun hideMoonCalendarDetailsDialog() {
        _showMoonCalendarDetailsDialogStateFlow.value = false
    }
}
