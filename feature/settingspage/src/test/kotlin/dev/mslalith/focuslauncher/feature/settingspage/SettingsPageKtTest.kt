package dev.mslalith.focuslauncher.feature.settingspage

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.overlay.rememberOverlayHost
import com.slack.circuit.runtime.screen.Screen
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.screens.AboutScreen
import dev.mslalith.focuslauncher.core.screens.AppDrawerSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.ClockWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.EditFavoritesScreen
import dev.mslalith.focuslauncher.core.screens.HideAppsScreen
import dev.mslalith.focuslauncher.core.screens.IconPackScreen
import dev.mslalith.focuslauncher.core.screens.LunarPhaseWidgetSettingsBottomSheetScreen
import dev.mslalith.focuslauncher.core.screens.ThemeSelectionBottomSheetScreen
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.compose.assertion.waitForTagAndAssertIsDisplayed
import dev.mslalith.focuslauncher.core.testing.compose.assertion.waitForTextAndAssertIsDisplayed
import dev.mslalith.focuslauncher.core.testing.compose.extensions.performScrollToAndClick
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasWidgetType
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config

@RunWith(AppRobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class SettingsPageKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var currentScreen: Screen? by mutableStateOf(value = null)
    private var bottomSheetScreen: Screen? by mutableStateOf(value = null)
    private var state by mutableStateOf(value = stateWith())

    @Before
    fun setup() {
        currentScreen = null
        bottomSheetScreen = null
        state = stateWith()
        composeTestRule.initializeWith()
    }

    @Test
    fun `01 - when change theme is clicked, theme selection bottom sheet must be shown`(): Unit = with(composeTestRule) {
        assertThat(bottomSheetScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_CHANGE_THEME).performScrollToAndClick()
        assertThat(bottomSheetScreen).isEqualTo(ThemeSelectionBottomSheetScreen)
    }

    @Test
    fun `02 - when edit favorites is clicked, user must be navigated to it's screen`(): Unit = with(composeTestRule) {
        assertThat(currentScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_EDIT_FAVORITES).performScrollToAndClick()
        assertThat(currentScreen).isEqualTo(EditFavoritesScreen)
    }

    @Test
    fun `03 - when hide apps is clicked, user must be navigated to it's screen`(): Unit = with(composeTestRule) {
        assertThat(currentScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_HIDE_APPS).performScrollToAndClick()
        assertThat(currentScreen).isEqualTo(HideAppsScreen)
    }

    @Test
    fun `04 - when toggle status bar is clicked, it's value must be toggled`(): Unit = with(composeTestRule) {
        val hideStatusBar = activity.getString(R.string.hide_status_bar)
        val showStatusBar = activity.getString(R.string.show_status_bar)

        waitForTextAndAssertIsDisplayed(text = showStatusBar)
        onNodeWithTag(testTag = TestTags.ITEM_TOGGLE_STATUS_BAR).performScrollToAndClick()
        waitForTextAndAssertIsDisplayed(text = hideStatusBar)
    }

    @Test
    fun `05 - when pull down notifications is clicked, it's value must be toggled`(): Unit = with(composeTestRule) {
        val disableText = activity.getString(R.string.disable_pull_down_notifications)
        val enableText = activity.getString(R.string.enable_pull_down_notifications)

        waitForTextAndAssertIsDisplayed(text = disableText)
        onNodeWithTag(testTag = TestTags.ITEM_TOGGLE_PULL_DOWN_NOTIFICATION).performScrollToAndClick()
        waitForTextAndAssertIsDisplayed(text = enableText)
    }

    @Test
    fun `06 - when icon packs is clicked, user must be navigated to it's screen`(): Unit = with(composeTestRule) {
        assertThat(currentScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_ICON_PACK).performScrollToAndClick()
        assertThat(currentScreen).isEqualTo(IconPackScreen)
    }

    @Test
    fun `07 - when app drawer is clicked, it's bottom sheet must be shown`(): Unit = with(composeTestRule) {
        assertThat(bottomSheetScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_APP_DRAWER).performScrollToAndClick()
        assertThat(bottomSheetScreen).isEqualTo(AppDrawerSettingsBottomSheetScreen)
    }

    @Test
    fun `08 - when widgets is clicked, it's section must be expanded`(): Unit = with(composeTestRule) {
        val clockText = activity.getString(R.string.clock)

        onNodeWithText(text = clockText).assertDoesNotExist()
        onNodeWithTag(testTag = TestTags.ITEM_WIDGETS).performScrollToAndClick()
        onNode(matcher = hasWidgetType(widgetType = WidgetType.CLOCK)).apply {
            performScrollTo()
            assertIsDisplayed()
        }
    }

    @Test
    fun `09 - on click of clock widget, it's bottom sheet must be shown`(): Unit = with(composeTestRule) {
        assertThat(bottomSheetScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_WIDGETS).performScrollToAndClick()
        onNode(matcher = hasWidgetType(widgetType = WidgetType.CLOCK)).performScrollToAndClick()
        assertThat(bottomSheetScreen).isEqualTo(ClockWidgetSettingsBottomSheetScreen)
    }

    @Test
    fun `10 - when lunar phase widget is clicked, it's bottom sheet must be shown`(): Unit = with(composeTestRule) {
        assertThat(bottomSheetScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_WIDGETS).performScrollToAndClick()
        onNode(matcher = hasWidgetType(widgetType = WidgetType.LUNAR_PHASE)).performScrollToAndClick()
        assertThat(bottomSheetScreen).isEqualTo(LunarPhaseWidgetSettingsBottomSheetScreen)
    }

    @Test
    fun `11 - when set as default is clicked & selected to be default, app must hide the view`(): Unit = with(composeTestRule) {
        onNodeWithTag(testTag = TestTags.ITEM_SET_AS_DEFAULT).apply {
            performScrollTo()
            assertIsDisplayed()
        }

        state = state.copy(isDefaultLauncher = true)

        onNodeWithTag(testTag = TestTags.ITEM_SET_AS_DEFAULT).assertDoesNotExist()
    }

    @Test
    fun `12 - when set as default is clicked & selected to be not default, app must not hide the view`(): Unit = with(composeTestRule) {
        onNodeWithTag(testTag = TestTags.ITEM_SET_AS_DEFAULT).apply {
            performScrollTo()
            assertIsDisplayed()
        }

        state = state.copy(isDefaultLauncher = false)

        waitForTagAndAssertIsDisplayed(testTag = TestTags.ITEM_SET_AS_DEFAULT)
    }

    @Test
    fun `13 - when about is clicked, user must be navigated to it's screen`(): Unit = with(composeTestRule) {
        assertThat(currentScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_ABOUT).performScrollToAndClick()
        assertThat(currentScreen).isEqualTo(AboutScreen)
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>.initializeWith() {
        setContent {
            ProvideSystemUiController {
                CompositionLocalProvider(LocalOverlayHost provides rememberOverlayHost()) {
                    SettingsPage(state = state)
                }
            }
        }
    }

    private fun stateWith(): SettingsPageState = SettingsPageState(
        showStatusBar = false,
        canDrawNotificationShade = true,
        showIconPack = true,
        isDefaultLauncher = false,
        showDeveloperOption = false,
        eventSink = { event ->
            state = when (event) {
                is SettingsPageUiEvent.GoTo -> state.also { currentScreen = event.screen }
                is SettingsPageUiEvent.OnBottomSheetOpened -> state.also { bottomSheetScreen = event.screen }
                is SettingsPageUiEvent.RefreshIsDefaultLauncher -> state
                SettingsPageUiEvent.ToggleNotificationShade -> state.copy(canDrawNotificationShade = !state.canDrawNotificationShade)
                SettingsPageUiEvent.ToggleStatusBarVisibility -> state.copy(showStatusBar = !state.showStatusBar)
            }
        }
    )
}
