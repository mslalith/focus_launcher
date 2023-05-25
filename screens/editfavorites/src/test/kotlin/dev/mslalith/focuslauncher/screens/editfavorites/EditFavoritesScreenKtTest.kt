package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedApp
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.compose.assertion.assertSelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.assertion.waitForTextAndAssertIsDisplayed
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForApp
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForTag
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.screens.editfavorites.model.EditFavoritesState
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class EditFavoritesScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
    private val allApps = TestApps.all.toSelectedApps()
    private var state: EditFavoritesState by mutableStateOf(value = stateWith(apps = allApps))

    @Before
    fun setup() {
        state = stateWith(apps = allApps)
        composeTestRule.initializeWith()
    }

    @Test
    fun `01 - initially favorites must not be selected`() = with(composeTestRule) {
        state.favoriteApps.forEach { selectedApp ->
            waitForApp(selectedApp = selectedApp)
            onNodeWithTag(testTag = selectedApp.app.packageName).assertSelectedApp(selectedApp = selectedApp)
        }
    }

    @Test
    fun `02 - when all apps are added to favorite, every item in the list must be selected`() = with(composeTestRule) {
        state.favoriteApps.forEach { selectedApp ->
            waitForApp(selectedApp = selectedApp)
            onNodeWithTag(testTag = selectedApp.app.packageName).assertSelectedApp(selectedApp = selectedApp)
        }

        state.favoriteApps.forEach { selectedApp ->
            onNodeWithTag(testTag = selectedApp.app.packageName).performClick()
        }

        state.favoriteApps.forEach { selectedApp ->
            waitForApp(selectedApp = selectedApp)
            onNodeWithTag(testTag = selectedApp.app.packageName).assertSelectedApp(selectedApp = selectedApp)
        }
    }

    @Test
    fun `03 - when favorites are cleared, every item in the list must not be selected`() = with(composeTestRule) {
        state.favoriteApps.forEach { selectedApp ->
            waitForTag(testTag = selectedApp.app.packageName)
            onNodeWithTag(testTag = selectedApp.app.packageName).performClick()
        }

        state.favoriteApps.forEach { selectedApp ->
            waitForApp(selectedApp = selectedApp)
            onNodeWithTag(testTag = selectedApp.app.packageName).assertSelectedApp(selectedApp = selectedApp)
        }

        onNodeWithTag(testTag = TestTags.TAG_CLEAR_FAVORITES_FAB).performClick()

        state.favoriteApps.forEach { selectedApp ->
            waitForApp(selectedApp = selectedApp)
            onNodeWithTag(testTag = selectedApp.app.packageName).assertSelectedApp(selectedApp = selectedApp)
        }
    }

    @Test
    fun `04 - when hidden apps are not shown & an app is hidden, it should not be listed in favorites`() = with(composeTestRule) {
        state = state.copy(
            favoriteApps = state.favoriteApps.filterNot { hiddenApps.contains(it.app) }.toImmutableList(),
            showHiddenApps = false
        )

        hiddenApps.forEach { app ->
            onNodeWithTag(testTag = app.packageName).assertDoesNotExist()
        }
    }

    @Test
    fun `05 - when hidden apps are shown & an app is hidden, it should be listed in favorites as disabled`() = with(composeTestRule) {
        state = state.withHiddenApps(apps = hiddenApps).copy(showHiddenApps = true)

        hiddenApps.forEach { app ->
            val selectedApp = app.toSelectedApp(isSelected = false, disabled = true)
            waitForApp(selectedApp = selectedApp)
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(selectedApp = selectedApp)
        }
    }

    @Test
    fun `06 - when hidden apps are shown & clicked on a hidden app, a snackbar should be shown`(): Unit = with(composeTestRule) {
        val hiddenApp = hiddenApps.first()
        state = state.withHiddenApps(apps = listOf(hiddenApp)).copy(showHiddenApps = true)

        onNodeWithTag(testTag = hiddenApp.packageName).apply {
            val selectedApp = hiddenApp.toSelectedApp(isSelected = false, disabled = true)
            waitForApp(selectedApp = selectedApp)
            assertSelectedApp(selectedApp = selectedApp)
            performClick()
        }

        val message = activity.getString(R.string.app_hidden_message, hiddenApp.name)
        waitForTextAndAssertIsDisplayed(text = message)
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>.initializeWith(
        goBack: () -> Unit = {}
    ) {
        setContent {
            ProvideSystemUiController {
                EditFavoritesScreenInternal(
                    editFavoritesState = state,
                    onToggleHiddenApps = { state = state.copy(showHiddenApps = it) },
                    onClearFavorites = {
                        state = state.copy(
                            favoriteApps = state.favoriteApps.map {
                                it.copy(isSelected = false)
                            }.toImmutableList()
                        )
                    },
                    onAddToFavorites = { state = state.copy(favoriteApps = state.favoriteApps.toggleAppSelected(app = it, isSelected = true)) },
                    onRemoveFromFavorites = { state = state.copy(favoriteApps = state.favoriteApps.toggleAppSelected(app = it, isSelected = false)) },
                    goBack = goBack
                )
            }
        }
    }
}

private fun stateWith(apps: List<SelectedApp>): EditFavoritesState = EditFavoritesState(
    favoriteApps = apps.toImmutableList(),
    showHiddenApps = false
)

private fun List<App>.toSelectedApps(
    isSelected: Boolean = false,
    disabled: Boolean = false
): List<SelectedApp> = map {
    it.toSelectedApp(
        isSelected = isSelected,
        disabled = disabled
    )
}

private fun App.toSelectedApp(
    isSelected: Boolean = false,
    disabled: Boolean = false
): SelectedApp = SelectedApp(
    app = this,
    isSelected = isSelected,
    disabled = disabled
)

private fun EditFavoritesState.withHiddenApps(apps: List<App>): EditFavoritesState = copy(
    favoriteApps = favoriteApps.map { favoriteApp ->
        apps.firstOrNull { it.packageName == favoriteApp.app.packageName }
            ?.toSelectedApp(isSelected = false, disabled = true)
            ?: favoriteApp
    }.toImmutableList()
)

private fun ImmutableList<SelectedApp>.toggleAppSelected(app: App, isSelected: Boolean): ImmutableList<SelectedApp> = map {
    if (it.app.packageName == app.packageName) it.copy(isSelected = isSelected)
    else it
}.toImmutableList()
