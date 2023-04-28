package dev.mslalith.focuslauncher.screens.editfavorites

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
import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.core.model.SelectedApp
import dev.mslalith.focuslauncher.core.testing.TestApps
import dev.mslalith.focuslauncher.core.testing.compose.assertion.assertSelectedApp
import dev.mslalith.focuslauncher.core.testing.compose.extensions.printToConsole
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags
import javax.inject.Inject
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

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    application = HiltTestApplication::class,
    instrumentedPackages = ["androidx.loader.content"]
)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class EditFavoritesScreenKtTest {

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

    private lateinit var viewModel: EditFavoritesViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = EditFavoritesViewModel(
            appDrawerRepo = appDrawerRepo,
            favoritesRepo = favoritesRepo,
            hiddenAppsRepo = hiddenAppsRepo,
            appCoroutineDispatcher = appCoroutineDispatcher
        )
        runBlocking {
            appDrawerRepo.addApps(apps = TestApps.all)
        }
        composeTestRule.setContent {
            EditFavoritesScreen(
                editFavoritesViewModel = viewModel,
                goBack = {}
            )
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
    fun `1 - initially favorites must not be selected`() = with(composeTestRule) {
        TestApps.all.forEach { app ->
            onNodeWithTag(testTag = app.packageName).printToConsole()
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = false)
            )
        }
    }

    @Test
    fun `2 - when all apps are added to favorite, every item in the list must be selected`() = with(composeTestRule) {
        TestApps.all.forEach { app ->
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = false)
            )
        }

        TestApps.all.forEach { app ->
            onNodeWithTag(testTag = app.packageName).performClick()
        }

        TestApps.all.forEach { app ->
            onNodeWithTag(testTag = app.packageName).printToConsole()
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = true)
            )
        }
    }

    @Test
    fun `3 - when favorites are cleared, every item in the list must not be selected`() = with(composeTestRule) {
        TestApps.all.forEach { app ->
            onNodeWithTag(testTag = app.packageName).performClick()
        }

        TestApps.all.forEach { app ->
            onNodeWithTag(testTag = app.packageName).printToConsole()
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = true)
            )
        }

        onNodeWithTag(testTag = TestTags.TAG_CLEAR_FAVORITES_FAB).performClick()

        TestApps.all.forEach { app ->
            onNodeWithTag(testTag = app.packageName).printToConsole()
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = false)
            )
        }
    }

    @Test
    fun `4 - when hidden apps are not shown & an app is hidden, it should not be listed in favorites`() = with(composeTestRule) {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val apps = TestApps.all - hiddenApps.toSet()
        runBlocking { hiddenAppsRepo.addToHiddenApps(apps = hiddenApps) }

        onNodeWithTag(testTag = TestTags.TAG_TOGGLE_HIDDEN_APPS).performClick()
        onNodeWithTag(testTag = TestTags.TAG_TOGGLE_HIDDEN_APPS).performClick()

        apps.forEach { app ->
            onNodeWithTag(testTag = app.packageName).printToConsole()
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = false)
            )
        }

        hiddenApps.forEach { app ->
            onNodeWithTag(testTag = app.packageName).assertDoesNotExist()
        }
    }

    @Test
    fun `5 - when hidden apps are shown & an app is hidden, it should be listed in favorites as disabled`() = with(composeTestRule) {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val apps = TestApps.all - hiddenApps.toSet()
        runBlocking { hiddenAppsRepo.addToHiddenApps(apps = hiddenApps) }

        onNodeWithTag(testTag = TestTags.TAG_TOGGLE_HIDDEN_APPS).performClick()

        apps.forEach { app ->
            onNodeWithTag(testTag = app.packageName).printToConsole()
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = false, disabled = false)
            )
        }

        hiddenApps.forEach { app ->
            onNodeWithTag(testTag = app.packageName).printToConsole()
            onNodeWithTag(testTag = app.packageName).assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = false, disabled = true)
            )
        }
    }

    @Test
    fun `6 - when hidden apps are shown & clicked on a hidden app, a snackbar should be shown`(): Unit = with(composeTestRule) {
        val hiddenApp = TestApps.Phone
        runBlocking { hiddenAppsRepo.addToHiddenApps(apps = listOf(hiddenApp)) }

        onNodeWithTag(testTag = TestTags.TAG_TOGGLE_HIDDEN_APPS).performClick()

        onNodeWithTag(testTag = hiddenApp.packageName).apply {
            printToConsole()
            assertSelectedApp(selectedApp = hiddenApp.toSelectedAppWith(isSelected = false, disabled = true))
            performClick()
        }

        val message = activity.getString(R.string.app_hidden_message).replace(oldValue = "{}", newValue = hiddenApp.name)
        onNodeWithText(text = message).assertIsDisplayed()
    }
}

private fun App.toSelectedAppWith(
    isSelected: Boolean = false,
    disabled: Boolean = false
): SelectedApp = SelectedApp(app = this, isSelected = isSelected, disabled = disabled)
