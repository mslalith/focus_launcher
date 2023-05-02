package dev.mslalith.focuslauncher.screens.hideapps

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.database.utils.CloseDatabase
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.app.SelectedHiddenApp
import dev.mslalith.focuslauncher.core.resources.R
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.compose.assertion.assertSelectedHiddenApp
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForApp
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForTag
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.screens.hideapps.utils.TestTags
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
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
class HideAppsScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDrawerRepo: AppDrawerRepo

    @Inject
    lateinit var favoritesRepo: FavoritesRepo

    @Inject
    lateinit var hiddenAppsRepo: HiddenAppsRepo

    @Inject
    lateinit var closeDatabase: CloseDatabase

    @Inject
    lateinit var appCoroutineDispatcher: AppCoroutineDispatcher

    private lateinit var viewModel: HideAppsViewModel

    private val favoriteApp = TestApps.Chrome

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = HideAppsViewModel(
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = favoritesRepo,
            hiddenAppsRepo = hiddenAppsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
        runBlocking {
            appDrawerRepo.addApps(apps = TestApps.all)
            favoritesRepo.addToFavorites(app = favoriteApp)
        }
        composeTestRule.setContent {
            ProvideSystemUiController {
                HideAppsScreen(
                    hideAppsViewModel = viewModel,
                    goBack = {}
                )
            }
        }
    }

    @After
    fun teardown() {
        runBlocking {
            favoritesRepo.clearFavorites()
            hiddenAppsRepo.clearHiddenApps()
            appDrawerRepo.clearApps()
            closeDatabase()
        }
    }

    @Test
    fun `1 - initially hidden apps must not be selected`() = with(composeTestRule) {
        TestApps.all.forEach { app ->
            val selectedHiddenApp = app.toSelectedHiddenAppWith(isSelected = false, isFavorite = app.packageName == favoriteApp.packageName)
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }
    }

    @Test
    fun `2 - when un-favorite apps are hidden, they must be selected`() = with(composeTestRule) {
        val unFavoriteApps = TestApps.all - favoriteApp

        unFavoriteApps.forEach { app ->
            val selectedHiddenApp = app.toSelectedHiddenAppWith(isSelected = false, isFavorite = false)
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }

        unFavoriteApps.forEach { app ->
            onNodeWithTag(testTag = app.packageName).performClick()
        }

        unFavoriteApps.forEach { app ->
            val selectedHiddenApp = app.toSelectedHiddenAppWith(isSelected = true, isFavorite = false)
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }
    }

    @Test
    fun `3 - when hidden apps are cleared, every item in the list must not be selected`() = with(composeTestRule) {
        val unFavoriteApps = TestApps.all - favoriteApp
        unFavoriteApps.forEach { app ->
            waitForTag(testTag = app.packageName)
            onNodeWithTag(testTag = app.packageName).performClick()
        }

        unFavoriteApps.forEach { app ->
            val selectedHiddenApp = app.toSelectedHiddenAppWith(isSelected = true, isFavorite = false)
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }

        onNodeWithTag(testTag = TestTags.TAG_CLEAR_HIDDEN_APPS_FAB).performClick()

        unFavoriteApps.forEach { app ->
            val selectedHiddenApp = app.toSelectedHiddenAppWith(isSelected = false, isFavorite = false)
            waitForApp(selectedHiddenApp = selectedHiddenApp)
            onNodeWithTag(testTag = app.packageName).assertSelectedHiddenApp(selectedHiddenApp = selectedHiddenApp)
        }
    }

    @Test
    fun `4 - when a favorite app is being hidden, confirmation UI must be shown`(): Unit = with(composeTestRule) {
        waitForTag(testTag = favoriteApp.packageName)
        onNodeWithTag(testTag = favoriteApp.packageName).performClick()

        val message = activity.getString(R.string.hide_favorite_app_message, favoriteApp.displayName)
        onNodeWithText(text = message).assertIsDisplayed()
    }

    @Test
    fun `5 - when a favorite app is being hidden, on click of cancel in confirmation UI, it should dismiss & no action must be taken`(): Unit = with(composeTestRule) {
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
    fun `6 - when a favorite app is being hidden, on click of hide in confirmation UI, it should be hidden`(): Unit = with(composeTestRule) {
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

private fun App.toSelectedHiddenAppWith(
    isSelected: Boolean,
    isFavorite: Boolean
): SelectedHiddenApp = SelectedHiddenApp(
    app = this,
    isSelected = isSelected,
    isFavorite = isFavorite
)
