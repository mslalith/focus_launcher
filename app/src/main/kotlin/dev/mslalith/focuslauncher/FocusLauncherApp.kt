package dev.mslalith.focuslauncher

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.HiltAndroidApp
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import dev.mslalith.focuslauncher.core.settings.sentry.SentrySettings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
@IgnoreInKoverReport
class FocusLauncherApp : Application() {

    @Inject
    lateinit var sentrySettings: SentrySettings

    override fun onCreate() {
        super.onCreate()

        setupSentry()
    }

    private fun setupSentry() = with(sentrySettings) {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            if (isEnabled.first()) enableSentry() else disableSentry()
        }
    }
}
