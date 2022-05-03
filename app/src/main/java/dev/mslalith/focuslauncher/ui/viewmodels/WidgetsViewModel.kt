package dev.mslalith.focuslauncher.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mslalith.focuslauncher.data.models.Outcome
import dev.mslalith.focuslauncher.data.repository.ClockRepo
import dev.mslalith.focuslauncher.data.repository.ClockRepo.Companion.INITIAL_TIME_OUTCOME
import dev.mslalith.focuslauncher.data.repository.LunarPhaseDetailsRepoImpl.Companion.INITIAL_LUNAR_PHASE_DETAILS_OUTCOME
import dev.mslalith.focuslauncher.data.repository.LunarPhaseDetailsRepoImpl.Companion.INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME
import dev.mslalith.focuslauncher.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.data.repository.interfaces.LunarPhaseDetailsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class WidgetsViewModel @Inject constructor(
    private val clockRepo: ClockRepo,
    lunarPhaseRepo: LunarPhaseDetailsRepo,
    private val quotesRepo: QuotesRepo
) : ViewModel() {

    init {
        launch {
            quotesRepo.nextRandomQuote()
        }
    }

    /**
     * Clock
     */
    val currentTimeStateFlow = clockRepo.currentTimeStateFlow.withinScope(INITIAL_TIME_OUTCOME)

    fun refreshTime() = clockRepo.refreshTime()

    /**
     * Lunar Phase
     */
    val lunarPhaseDetailsStateFlow = lunarPhaseRepo.lunarPhaseDetailsStateFlow.withinScope(INITIAL_LUNAR_PHASE_DETAILS_OUTCOME)
    val upcomingLunarPhaseStateFlow = lunarPhaseRepo.upcomingLunarPhaseStateFlow.withinScope(INITIAL_UPCOMING_LUNAR_PHASE_OUTCOME)

    /**
     * Quotes
     */
    val currentQuoteOutcomeStateFlow = quotesRepo.currentQuoteStateFlow.withinScope(Outcome.None)
    val isFetchingQuotes = quotesRepo.isFetchingQuotesStateFlow.withinScope(false)

    fun nextRandomQuote() { launch { quotesRepo.nextRandomQuote() } }

    fun fetchQuotesIfRequired() {
        launch {
            if (quotesRepo.hasQuotesReachedLimit()) return@launch
            quotesRepo.fetchQuotes(maxPages = 2)
        }
    }

    private fun launch(
        coroutineContext: CoroutineContext = Dispatchers.Main,
        run: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(coroutineContext) { run() }

    private fun <T> Flow<T>.withinScope(
        initialValue: T
    ) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = initialValue
    )
}
