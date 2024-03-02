package dev.mslalith.focuslauncher.core.settings.sentry

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSentrySettings : SentrySettings {

    private val _isEnabledStateFlow = MutableStateFlow(value = true)
    override val isEnabled: Flow<Boolean> = _isEnabledStateFlow

    private var sentryDsnInternal: String = ""

    override fun enableSentry() = Unit

    override fun disableSentry() = Unit

    override fun getSentryDsn(): String = sentryDsnInternal

    fun setSentryDsn(dsn: String) {
        sentryDsnInternal = dsn
    }
}
