package dev.mslalith.focuslauncher.core.data.test.repository.settings

import dev.mslalith.focuslauncher.core.data.repository.settings.QuotesSettingsRepo
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.Quotes.DEFAULT_SHOW_QUOTES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeQuotesSettingsRepo : QuotesSettingsRepo {

    private val showQuotesStateFlow = MutableStateFlow(value = DEFAULT_SHOW_QUOTES)
    override val showQuotesFlow: Flow<Boolean> = showQuotesStateFlow

    override suspend fun toggleShowQuotes() {
        showQuotesStateFlow.update { !it }
    }
}
