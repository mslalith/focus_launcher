package dev.mslalith.focuslauncher.screens.hideapps

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.resources.R
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.compose.assertion.assertSelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForApp
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForTag
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.screens.hideapps.model.HideAppsState
import dev.mslalith.focuslauncher.screens.hideapps.utils.TestTags
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    application = HiltTestApplication::class,
    instrumentedPackages = ["androidx.loader.content"]
)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class HideAppsScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val favoriteApp = TestApps.Chrome

    @Test
    fun `01 - initially hidden apps must not be selected`() = with(composeTestRule) {
        val allApps = TestApps.all.toSelectedHiddenAppWith()
        initializeWith(state = stateWith(apps = allApps))

        allApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }
    }

    @Test
    fun `02 - when un-favorite apps are hidden, they must be selected`() = with(composeTestRule) {
        val allApps = TestApps.all.toSelectedHiddenAppWith()
        var state by mutableStateOf(value = stateWith(apps = allApps))

        initializeWith(
            state = state,
            onAddToHiddenApps = { app ->
                state = state.copy(
                    hiddenApps = state.hiddenApps.toggleAppSelected(app = app)
                )
            }
        )

        allApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }

        allApps.forEach { selectedHiddenApp ->
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).performClick()
        }

        allApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }
    }

    @Test
    fun `03 - when hidden apps are cleared, every item in the list must not be selected`() = with(composeTestRule) {
        val allApps = TestApps.all.toSelectedHiddenAppWith(isSelected = true)
        var state by mutableStateOf(value = stateWith(apps = allApps))

        initializeWith(
            state = state,
            onAddToHiddenApps = { app ->
                state = state.copy(
                    hiddenApps = state.hiddenApps.toggleAppSelected(app = app)
                )
            }
        )

        allApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }

        onNodeWithTag(testTag = TestTags.TAG_CLEAR_HIDDEN_APPS_FAB).performClick()

        allApps.forEach { selectedHiddenApp ->
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = selectedHiddenApp.app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }
    }

    @Test
    fun `04 - when a favorite app is being hidden, confirmation UI must be shown`(): Unit = with(composeTestRule) {
        val allApps = TestApps.all.toSelectedHiddenAppWith().withFavorite(app = favoriteApp)
        initializeWith(state = stateWith(apps = allApps))

        waitForTag(testTag = favoriteApp.packageName)
        onNodeWithTag(testTag = favoriteApp.packageName).performClick()

        val message = activity.getString(R.string.hide_favorite_app_message, favoriteApp.displayName)
        onNodeWithText(text = message).assertIsDisplayed()
    }

    @Test
    fun `05 - when a favorite app is being hidden, on click of cancel in confirmation UI, it should dismiss & no action must be taken`(): Unit = with(composeTestRule) {
        val allApps = TestApps.all.toSelectedHiddenAppWith().withFavorite(app = favoriteApp)
        initializeWith(state = stateWith(apps = allApps))

        waitForTag(testTag = favoriteApp.packageName)
        onNodeWithTag(testTag = favoriteApp.packageName).performClick()

        val message = activity.getString(R.string.hide_favorite_app_message, favoriteApp.displayName)
        onNodeWithText(text = message).assertIsDisplayed()

        val text = activity.getString(R.string.cancel)
        onNodeWithText(text = text).performClick()

        val selectedHiddenApp = favoriteApp.toSelectedHiddenAppWith(isSelected = false, isFavorite = true)
        waitForApp(selectedHiddenApp = selectedHiddenApp)
        onNodeWithTag(testTag = favoriteApp.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
    }

    @Test
    fun `06 - when a favorite app is being hidden, on click of hide in confirmation UI, it should be hidden`(): Unit = with(composeTestRule) {
        val allApps = TestApps.all.toSelectedHiddenAppWith().withFavorite(app = favoriteApp)
        var state by mutableStateOf(value = stateWith(apps = allApps))

        initializeWith(
            state = state,
            onAddToHiddenApps = { app ->
                state = state.copy(
                    hiddenApps = state.hiddenApps.toggleAppSelected(app = app)
                )
            },
            onRemoveFromFavorites = { app ->
                state = state.copy(
                    hiddenApps = state.hiddenApps.toggleAppFavorite(app = app)
                )
            }
        )

        waitForTag(testTag = favoriteApp.packageName)
        onNodeWithTag(testTag = favoriteApp.packageName).performClick()

        val message = activity.getString(R.string.hide_favorite_app_message, favoriteApp.displayName)
        onNodeWithText(text = message).assertIsDisplayed()
        val text = activity.getString(R.string.yes_comma_hide)
        onNodeWithText(text = text).performClick()

        onNodeWithText(text = message).assertDoesNotExist()

        val selectedHiddenApp = favoriteApp.toSelectedHiddenAppWith(isSelected = true, isFavorite = false)
        waitForApp(selectedHiddenApp = selectedHiddenApp)
        onNodeWithTag(testTag = favoriteApp.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
    }
}

private fun AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>.initializeWith(
    state: HideAppsState,
    onClearHiddenApps: () -> Unit = {},
    onRemoveFromFavorites: (App) -> Unit = {},
    onAddToHiddenApps: (App) -> Unit = {},
    onRemoveFromHiddenApps: (App) -> Unit = {},
    goBack: () -> Unit = {}
) {
    setContent {
        ProvideSystemUiController {
            HideAppsScreenInternal(
                hideAppsState = state,
                onClearHiddenApps = onClearHiddenApps,
                onRemoveFromFavorites = onRemoveFromFavorites,
                onAddToHiddenApps = onAddToHiddenApps,
                onRemoveFromHiddenApps = onRemoveFromHiddenApps,
                goBack = goBack
            )
        }
    }
}

private fun stateWith(apps: List<SelectedHiddenApp>): HideAppsState = HideAppsState(hiddenApps = apps.toImmutableList())

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

private fun List<SelectedHiddenApp>.withFavorite(app: App): List<SelectedHiddenApp> = map {
    if (it.app.packageName == app.packageName) it.copy(isFavorite = true) else it
}

private fun ImmutableList<SelectedHiddenApp>.toggleAppSelected(app: App): ImmutableList<SelectedHiddenApp> = map {
    if (it.app.packageName == app.packageName) it.copy(isSelected = !it.isSelected)
    else it
}.toImmutableList()

private fun ImmutableList<SelectedHiddenApp>.toggleAppFavorite(app: App): ImmutableList<SelectedHiddenApp> = map {
    if (it.app.packageName == app.packageName) it.copy(isFavorite = !it.isFavorite)
    else it
}.toImmutableList()
