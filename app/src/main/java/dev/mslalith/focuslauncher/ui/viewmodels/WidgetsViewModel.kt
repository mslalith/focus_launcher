package dev.mslalith.focuslauncher.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.data.model.State
import dev.mslalith.focuslauncher.data.repository.ClockRepo
import dev.mslalith.focuslauncher.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.data.repository.LunarPhaseDetailsRepo.Companion.INITIAL_LUNAR_PHASE_DETAILS_STATE
import dev.mslalith.focuslauncher.data.repository.LunarPhaseDetailsRepo.Companion.INITIAL_UPCOMING_LUNAR_PHASE_STATE
import dev.mslalith.focuslauncher.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.data.utils.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.extensions.formatToTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetsViewModel @Inject constructor(
    private val clockRepo: ClockRepo,
    private val lunarPhaseRepo: LunarPhaseDetailsRepo,
    private val quotesRepo: QuotesRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : ViewModel() {

    init {
        launch {
            quotesRepo.addInitialQuotesIfNeeded()
        }
        launch {
            clockRepo.currentInstantStateFlow.collectLatest { instant ->
                lunarPhaseRepo.refreshLunarPhaseDetails(instant)
            }
        }
    }

    /**
     * Clock
     */
    val currentTimeStateFlow = clockRepo.currentInstantStateFlow.map { it.formatToTime() }.withinScope("")

    fun refreshTime() = clockRepo.refreshTime()

    /**
     * Lunar Phase
     */
    val lunarPhaseDetailsStateFlow = lunarPhaseRepo.lunarPhaseDetailsStateFlow.withinScope(INITIAL_LUNAR_PHASE_DETAILS_STATE)
    val upcomingLunarPhaseStateFlow = lunarPhaseRepo.upcomingLunarPhaseStateFlow.withinScope(INITIAL_UPCOMING_LUNAR_PHASE_STATE)

    private val _showMoonCalendarDetailsDialogStateFlow = MutableStateFlow(false)
    val showMoonCalendarDetailsDialogStateFlow: StateFlow<Boolean>
        get() = _showMoonCalendarDetailsDialogStateFlow

    fun showMoonCalendarDetailsDialog() {
        _showMoonCalendarDetailsDialogStateFlow.value = true
    }

    fun hideMoonCalendarDetailsDialog() {
        _showMoonCalendarDetailsDialogStateFlow.value = false
    }

    /**
     * Quotes
     */
    val currentQuoteStateFlow = quotesRepo.currentQuoteStateFlow.withinScope(State.Initial)
    val isFetchingQuotes = quotesRepo.isFetchingQuotesStateFlow.withinScope(false)

    fun nextRandomQuote() { launch { quotesRepo.nextRandomQuote() } }

    fun fetchQuotesIfRequired() {
        launch {
            if (quotesRepo.hasQuotesReachedLimit()) return@launch
            quotesRepo.fetchQuotes(maxPages = 2)
        }
    }

    private fun launch(
        run: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(appCoroutineDispatcher.io) { run() }

    private fun <T> Flow<T>.withinScope(
        initialValue: T
    ) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue
    )
}
