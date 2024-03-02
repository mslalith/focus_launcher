package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.privacy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.settings.UpdateReportCrashesSettingUseCase
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_REPORT_CRASHES
import dev.mslalith.focuslauncher.core.screens.PrivacySettingsBottomSheetScreen
import dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.privacy.PrivacySettingsBottomSheetUiEvent.ToggleReportCrashes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PrivacySettingsBottomSheetPresenter @AssistedInject constructor(
    private val updateReportCrashesSettingUseCase: UpdateReportCrashesSettingUseCase,
    private val generalSettingsRepo: GeneralSettingsRepo,
    private val appCoroutineDispatcher: AppCoroutineDispatcher
) : Presenter<PrivacySettingsBottomSheetState> {

    @CircuitInject(PrivacySettingsBottomSheetScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(): PrivacySettingsBottomSheetPresenter
    }

    @Composable
    override fun present(): PrivacySettingsBottomSheetState {
        val scope = rememberCoroutineScope()

        val reportCrashes by generalSettingsRepo.reportCrashesFlow.collectAsRetainedState(initial = DEFAULT_REPORT_CRASHES)

        return PrivacySettingsBottomSheetState(
            reportCrashes = reportCrashes
        ) {
            when (it) {
                ToggleReportCrashes -> scope.toggleReportCrashes()
            }
        }
    }

    private fun CoroutineScope.toggleReportCrashes() {
        launch(appCoroutineDispatcher.io) {
            updateReportCrashesSettingUseCase(enabled = !generalSettingsRepo.reportCrashesFlow.first())
        }
    }
}
