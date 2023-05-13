package dev.mslalith.focuslauncher.feature.settingspage

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.Lifecycle
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.model.Screen
import dev.mslalith.focuslauncher.core.model.WidgetType
import dev.mslalith.focuslauncher.core.testing.compose.assertion.waitForTagAndAssertIsDisplayed
import dev.mslalith.focuslauncher.core.testing.compose.assertion.waitForTextAndAssertIsDisplayed
import dev.mslalith.focuslauncher.core.testing.compose.extensions.performScrollToAndClick
import dev.mslalith.focuslauncher.core.testing.compose.matcher.hasWidgetType
import dev.mslalith.focuslauncher.core.ui.providers.ProvideBottomSheetManager
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.feature.settingspage.util.TestTags
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    application = HiltTestApplication::class,
    instrumentedPackages = ["androidx.loader.content"]
)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class SettingsPageKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var generalSettingsRepo: GeneralSettingsRepo

    @Inject
    lateinit var appDrawerSettingsRepo: AppDrawerSettingsRepo

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: SettingsPageViewModel

    private var currentScreen: Screen? by mutableStateOf(value = null)

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = SettingsPageViewModel(
            generalSettingsRepo = generalSettingsRepo,
            appDrawerSettingsRepo = appDrawerSettingsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
        currentScreen = null
        mockkStatic(Context::isDefaultLauncher)
        composeTestRule.setContent {
            ProvideBottomSheetManager {
                ProvideSystemUiController {
                    SettingsPageInternal(
                        settingsPageViewModel = viewModel,
                        navigateTo = { currentScreen = it }
                    )
                }
            }
        }
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    @Ignore("TODO")
    fun `01 - when change theme is clicked, theme selection bottom sheet must be shown`(): Unit = with(composeTestRule) {
        onNodeWithTag(testTag = TestTags.THEME_SELECTION_SHEET).assertDoesNotExist()
        onNodeWithTag(testTag = TestTags.ITEM_CHANGE_THEME).performScrollToAndClick()
        onNodeWithTag(testTag = TestTags.THEME_SELECTION_SHEET).assertIsDisplayed()
    }

    @Test
    fun `02 - when edit favorites is clicked, user must be navigated to it's screen`(): Unit = with(composeTestRule) {
        assertThat(currentScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_EDIT_FAVORITES).performScrollToAndClick()
        assertThat(currentScreen).isEqualTo(Screen.EditFavorites)
    }

    @Test
    fun `03 - when hide apps is clicked, user must be navigated to it's screen`(): Unit = with(composeTestRule) {
        assertThat(currentScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_HIDE_APPS).performScrollToAndClick()
        assertThat(currentScreen).isEqualTo(Screen.HideApps)
    }

    @Test
    fun `04 - when toggle status bar is clicked, it's value must be toggled`(): Unit = with(composeTestRule) {
        val hideStatusBar = activity.getString(R.string.hide_status_bar)
        val showStatusBar = activity.getString(R.string.show_status_bar)

        onNodeWithText(text = showStatusBar).assertIsDisplayed()

        onNodeWithTag(testTag = TestTags.ITEM_TOGGLE_STATUS_BAR).performScrollToAndClick()
        waitForTextAndAssertIsDisplayed(text = hideStatusBar)

        onNodeWithText(text = hideStatusBar).assertIsDisplayed()
    }

    @Test
    fun `05 - when pull down notifications is clicked, it's value must be toggled`(): Unit = with(composeTestRule) {
        val disableText = activity.getString(R.string.disable_pull_down_notifications)
        val enableText = activity.getString(R.string.enable_pull_down_notifications)

        onNodeWithText(text = disableText).assertIsDisplayed()

        onNodeWithTag(testTag = TestTags.ITEM_TOGGLE_PULL_DOWN_NOTIFICATION).performScrollToAndClick()
        waitForTextAndAssertIsDisplayed(text = enableText)

        onNodeWithText(text = enableText).assertIsDisplayed()
    }

    @Test
    fun `06 - when icon packs is clicked, user must be navigated to it's screen`(): Unit = with(composeTestRule) {
        assertThat(currentScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_ICON_PACK).performScrollToAndClick()
        assertThat(currentScreen).isEqualTo(Screen.IconPack)
    }

    @Test
    @Ignore("TODO")
    fun `07 - when app drawer is clicked, it's bottom sheet must be shown`(): Unit = with(composeTestRule) {
        onNodeWithTag(testTag = TestTags.APP_DRAWER_SETTINGS_SHEET).assertDoesNotExist()
        onNodeWithTag(testTag = TestTags.ITEM_APP_DRAWER).performScrollToAndClick()
        waitForTagAndAssertIsDisplayed(testTag = TestTags.APP_DRAWER_SETTINGS_SHEET)
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
    @Ignore("TODO")
    fun `09 - on click of clock widget, it's bottom sheet must be shown`(): Unit = with(composeTestRule) {
    }

    @Test
    @Ignore("TODO")
    fun `10 - when lunar phase widget is clicked, it's bottom sheet must be shown`(): Unit = with(composeTestRule) {
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `11 - when set as default is clicked & selected to be default, app must hide the view`(): Unit = with(composeTestRule) {
        every { activity.isDefaultLauncher() } returns true

        onNodeWithTag(testTag = TestTags.ITEM_SET_AS_DEFAULT).performScrollToAndClick()

        activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        activityRule.scenario.moveToState(Lifecycle.State.RESUMED)

        waitUntilDoesNotExist(matcher = hasTestTag(testTag = TestTags.ITEM_SET_AS_DEFAULT))
        onNodeWithTag(testTag = TestTags.ITEM_SET_AS_DEFAULT).assertDoesNotExist()
    }

    @Test
    fun `12 - when set as default is clicked & selected to be not default, app must not hide the view`(): Unit = with(composeTestRule) {
        every { activity.isDefaultLauncher() } returns false

        onNodeWithTag(testTag = TestTags.ITEM_SET_AS_DEFAULT).performScrollToAndClick()

        activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        activityRule.scenario.moveToState(Lifecycle.State.RESUMED)

        onNodeWithTag(testTag = TestTags.ITEM_SET_AS_DEFAULT).assertIsDisplayed()
    }

    @Test
    fun `13 - when about is clicked, user must be navigated to it's screen`(): Unit = with(composeTestRule) {
        assertThat(currentScreen).isNull()
        onNodeWithTag(testTag = TestTags.ITEM_ABOUT).performScrollToAndClick()
        assertThat(currentScreen).isEqualTo(Screen.About)
    }
}
