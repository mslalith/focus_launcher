package dev.mslalith.focuslauncher.screens.hideapps

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.resources.R
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.compose.assertion.assertSelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.assertion.waitForTextAndAssertIsDisplayed
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForApp
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForTag
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.screens.hideapps.HideAppsUiEvent.AddToHiddenApps
import dev.mslalith.focuslauncher.screens.hideapps.HideAppsUiEvent.ClearHiddenApps
import dev.mslalith.focuslauncher.screens.hideapps.HideAppsUiEvent.GoBack
import dev.mslalith.focuslauncher.screens.hideapps.HideAppsUiEvent.RemoveFromFavorites
import dev.mslalith.focuslauncher.screens.hideapps.HideAppsUiEvent.RemoveFromHiddenApps
import dev.mslalith.focuslauncher.screens.hideapps.utils.TestTags
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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
class HideAppsScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val favoriteApp = TestApps.Chrome
    private val allApps = TestApps.all.toSelectedHiddenAppWith()
    private var state by mutableStateOf(value = stateWith(apps = allApps))

    @Before
    fun setup() {
        state = stateWith(apps = allApps)
        composeTestRule.initializeWith()
    }

    @Test
    fun `01 - initially hidden apps must not be selected`() = with(composeTestRule) {
        state.hiddenApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }
    }

    @Test
    fun `02 - when un-favorite apps are hidden, they must be selected`() = with(composeTestRule) {
        state.hiddenApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }

        state.hiddenApps.forEach { selectedHiddenApp ->
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).performClick()
        }

        state.hiddenApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }
    }

    @Test
    fun `03 - when hidden apps are cleared, every item in the list must not be selected`() = with(composeTestRule) {
        state.hiddenApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }

        onNodeWithTag(testTag = TestTags.TAG_CLEAR_HIDDEN_APPS_FAB).performClick()

        assertThat(onAllNodesWithTag(testTag = TestTags.TAG_HIDDEN_APPS_LIST_ITEM).fetchSemanticsNodes()).isEmpty()
    }

    @Test
    fun `04 - when a favorite app is being hidden, confirmation UI must be shown`(): Unit = with(composeTestRule) {
        state = state.withFavorite(app = favoriteApp)

        waitForTag(testTag = favoriteApp.packageName)
        onNodeWithTag(testTag = favoriteApp.packageName).performClick()

        val message = activity.getString(R.string.hide_favorite_app_message, favoriteApp.displayName)
        waitForTextAndAssertIsDisplayed(text = message)
    }

    @Test
    fun `05 - when a favorite app is being hidden, on click of cancel in confirmation UI, it should dismiss & no action must be taken`(): Unit = with(composeTestRule) {
        state = state.withFavorite(app = favoriteApp)

        waitForTag(testTag = favoriteApp.packageName)
        onNodeWithTag(testTag = favoriteApp.packageName).performClick()

        val message = activity.getString(R.string.hide_favorite_app_message, favoriteApp.displayName)
        waitForTextAndAssertIsDisplayed(text = message)

        val text = activity.getString(R.string.cancel)
        onNodeWithText(text = text).performClick()

        val selectedHiddenApp = favoriteApp.toSelectedHiddenAppWith(isSelected = false, isFavorite = true)
        waitForApp(selectedHiddenApp = selectedHiddenApp)
        onNodeWithTag(testTag = favoriteApp.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
    }

    @Test
    fun `06 - when a favorite app is being hidden, on click of hide in confirmation UI, it should be hidden`(): Unit = with(composeTestRule) {
        state = state.withFavorite(app = favoriteApp)

        waitForTag(testTag = favoriteApp.packageName)
        onNodeWithTag(testTag = favoriteApp.packageName).performClick()

        val message = activity.getString(R.string.hide_favorite_app_message, favoriteApp.displayName)
        waitForTextAndAssertIsDisplayed(text = message)

        val text = activity.getString(R.string.yes_comma_hide)
        onNodeWithText(text = text).performClick()

        onNodeWithText(text = message).assertDoesNotExist()

        val selectedHiddenApp = favoriteApp.toSelectedHiddenAppWith(isSelected = true, isFavorite = false)

        waitForApp(selectedHiddenApp = selectedHiddenApp)
        onNodeWithTag(testTag = favoriteApp.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>.initializeWith() {
        setContent {
            ProvideSystemUiController {
                HideApps(state = state)
            }
        }
    }

    private fun stateWith(apps: List<SelectedHiddenApp>): HideAppsState = HideAppsState(
        hiddenApps = apps.toImmutableList(),
        eventSink = {
            state = when (it) {
                GoBack -> state
                ClearHiddenApps -> state.copy(hiddenApps = persistentListOf())
                is AddToHiddenApps -> state.copy(hiddenApps = state.hiddenApps.toggleAppSelected(app = it.app, isSelected = true))
                is RemoveFromFavorites -> state.copy(hiddenApps = state.hiddenApps.toggleAppFavorite(app = it.app, isFavorite = false))
                is RemoveFromHiddenApps -> state.copy(hiddenApps = state.hiddenApps.toggleAppSelected(app = it.app, isSelected = false))
            }
        }
    )
}

private fun List<App>.toSelectedHiddenAppWith(
    isSelected: Boolean = false,
    isFavorite: Boolean = false
): List<SelectedHiddenApp> = map {
    it.toSelectedHiddenAppWith(
        isSelected = isSelected,
        isFavorite = isFavorite
    )
}

private fun App.toSelectedHiddenAppWith(
    isSelected: Boolean,
    isFavorite: Boolean
): SelectedHiddenApp = SelectedHiddenApp(
    app = this,
    isSelected = isSelected,
    isFavorite = isFavorite
)

private fun HideAppsState.withFavorite(app: App): HideAppsState = copy(
    hiddenApps = hiddenApps.toggleAppFavorite(app = app, isFavorite = true)
)

private fun ImmutableList<SelectedHiddenApp>.toggleAppSelected(app: App, isSelected: Boolean): ImmutableList<SelectedHiddenApp> = map {
    if (it.app.packageName == app.packageName) it.copy(isSelected = isSelected)
    else it
}.toImmutableList()

private fun ImmutableList<SelectedHiddenApp>.toggleAppFavorite(app: App, isFavorite: Boolean): ImmutableList<SelectedHiddenApp> = map {
    if (it.app.packageName == app.packageName) it.copy(isFavorite = isFavorite)
    else it
}.toImmutableList()
