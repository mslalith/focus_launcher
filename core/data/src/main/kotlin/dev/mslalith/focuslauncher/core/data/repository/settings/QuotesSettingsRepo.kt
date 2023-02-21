package dev.mslalith.focuslauncher.core.data.repository.settings

import kotlinx.coroutines.flow.Flow

interface QuotesSettingsRepo {
    val showQuotesFlow: Flow<Boolean>

    suspend fun toggleShowQuotes()
}
