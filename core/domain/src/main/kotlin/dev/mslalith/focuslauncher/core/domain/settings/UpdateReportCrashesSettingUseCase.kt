package dev.mslalith.focuslauncher.core.domain.settings

import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.settings.sentry.SentrySettings
import javax.inject.Inject

class UpdateReportCrashesSettingUseCase @Inject constructor(
    private val sentrySettings: SentrySettings,
    private val generalSettingsRepo: GeneralSettingsRepo
) {
    suspend operator fun invoke(enabled: Boolean) {
        generalSettingsRepo.updateReportCrashes(enabled = enabled)
        with(sentrySettings) {
            if (enabled) enableSentry() else disableSentry()
        }
    }
}
