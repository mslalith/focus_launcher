package dev.mslalith.focuslauncher.feature.settingspage.bottomsheet.appdrawersettings

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.test.repository.settings.FakeAppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.circuit.PresenterTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AppRobolectricTestRunner::class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class AppDrawerSettingsBottomSheetPresenterTest : PresenterTest<AppDrawerSettingsBottomSheetPresenter, AppDrawerSettingsBottomSheetState>() {

    override fun presenterUnderTest() = AppDrawerSettingsBottomSheetPresenter(
        appDrawerSettingsRepo = FakeAppDrawerSettingsRepo(),
        appCoroutineDispatcher = TestAppCoroutineDispatcher()
    )

    @Test
    fun `01 - verify initial state`(): Unit = runPresenterTest {
        val state = awaitItem()
        assertThat(state.appDrawerViewType).isEqualTo(AppDrawerViewType.GRID)
        assertThat(state.showSearchBar).isEqualTo(true)
        assertThat(state.showAppGroupHeader).isEqualTo(true)
    }

    @Test
    fun `02 - when new apps view type is selected, the UI must update with new value`(): Unit = runPresenterTest {
        val state = awaitItem()
        assertThat(state.appDrawerViewType).isEqualTo(AppDrawerViewType.GRID)

        state.eventSink(AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerViewType(viewType = AppDrawerViewType.LIST))
        assertThat(awaitItem().appDrawerViewType).isEqualTo(AppDrawerViewType.LIST)
    }

    @Test
    fun `03 - when search bar his hidden, it's option should be disabled`(): Unit = runPresenterTest {
        val state = awaitItem()
        assertThat(state.showSearchBar).isTrue()

        state.eventSink(AppDrawerSettingsBottomSheetUiEvent.ToggleSearchBarVisibility)
        assertThat(awaitItem().showSearchBar).isFalse()
    }

    @Test
    fun `04 - when group apps by character is disabled, it's option should be disabled`(): Unit = runPresenterTest {
        val state = awaitItem()
        assertThat(state.showAppGroupHeader).isTrue()

        state.eventSink(AppDrawerSettingsBottomSheetUiEvent.ToggleAppGroupHeaderVisibility)
        assertThat(awaitItem().showAppGroupHeader).isFalse()
    }

    @Test
    fun `05 - when new app icon type is selected, the UI must update with new value`(): Unit = runPresenterTest {
        val state = awaitItem()
        assertThat(state.appDrawerIconViewType).isEqualTo(AppDrawerIconViewType.ICONS)

        state.eventSink(AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerIconViewType(viewType = AppDrawerIconViewType.COLORED))
        assertThat(awaitItem().appDrawerIconViewType).isEqualTo(AppDrawerIconViewType.COLORED)
    }

    @Test
    fun `06 - when app icon view type is text, on switching apps view type to grid, app icon view type must be set to icons`(): Unit = runPresenterTest {
        var state = awaitItem()

        state.eventSink(AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerViewType(viewType = AppDrawerViewType.LIST))
        state.eventSink(AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerIconViewType(viewType = AppDrawerIconViewType.TEXT))

        state = awaitItem()
        assertThat(state.appDrawerViewType).isEqualTo(AppDrawerViewType.LIST)

        state = awaitItem()
        assertThat(state.appDrawerIconViewType).isEqualTo(AppDrawerIconViewType.TEXT)

        state.eventSink(AppDrawerSettingsBottomSheetUiEvent.UpdateAppDrawerViewType(viewType = AppDrawerViewType.GRID))

        state = awaitItem()
        assertThat(state.appDrawerViewType).isEqualTo(AppDrawerViewType.GRID)
        assertThat(state.appDrawerIconViewType).isEqualTo(AppDrawerIconViewType.ICONS)
    }
}
