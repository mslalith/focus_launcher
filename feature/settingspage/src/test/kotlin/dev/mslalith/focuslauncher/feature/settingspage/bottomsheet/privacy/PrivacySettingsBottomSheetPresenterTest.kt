package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.privacy

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeGeneralSettingsRepo
import dev.mslalith.focuslauncher.core.domain.settings.UpdateReportCrashesSettingUseCase
import dev.mslalith.focuslauncher.core.settings.sentry.FakeSentrySettings
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class PrivacySettingsBottomSheetPresenterTest : PresenterTest<PrivacySettingsBottomSheetPresenter, PrivacySettingsBottomSheetState>() {

    private val generalSettingsRepo = FakeGeneralSettingsRepo()
    private val appCoroutineDispatcher = TestAppCoroutineDispatcher()
    private val sentrySettings = FakeSentrySettings()

    override fun presenterUnderTest() = PrivacySettingsBottomSheetPresenter(
        updateReportCrashesSettingUseCase = UpdateReportCrashesSettingUseCase(
            sentrySettings = sentrySettings,
            generalSettingsRepo = generalSettingsRepo
        ),
        generalSettingsRepo = generalSettingsRepo,
        appCoroutineDispatcher = appCoroutineDispatcher
    )

    @Test
    fun `01 - crash reports are enabled by default`() = runPresenterTest {
        assertForTrue { it.reportCrashes }
    }

    @Test
    fun `02 - when enabled, crash reports should be disabled on toggle`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.reportCrashes).isTrue()

        state.eventSink(PrivacySettingsBottomSheetUiEvent.ToggleReportCrashes)
        assertThat(awaitItem().reportCrashes).isFalse()
    }

    @Test
    fun `03 - when disabled, crash reports should be enabled on toggle`() = runPresenterTest {
        val state = awaitItem()
        assertThat(state.reportCrashes).isTrue()

        state.eventSink(PrivacySettingsBottomSheetUiEvent.ToggleReportCrashes)
        assertThat(awaitItem().reportCrashes).isFalse()

        state.eventSink(PrivacySettingsBottomSheetUiEvent.ToggleReportCrashes)
        assertThat(awaitItem().reportCrashes).isTrue()
    }
}
