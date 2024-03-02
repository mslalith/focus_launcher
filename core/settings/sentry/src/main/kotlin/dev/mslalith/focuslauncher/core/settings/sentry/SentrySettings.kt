package dev.mslalith.focuslauncher.core.settings.sentry

import kotlinx.coroutines.flow.Flow

interface SentrySettings {
    val isEnabled: Flow<Boolean>

    fun enableSentry()
    fun disableSentry()
    fun getSentryDsn(): String
}
