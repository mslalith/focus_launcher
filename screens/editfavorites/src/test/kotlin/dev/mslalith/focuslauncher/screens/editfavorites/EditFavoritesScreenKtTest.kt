package dev.mslalith.focuslauncher.screens.editfavorites

import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
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
import dev.mslalith.focuslauncher.core.testing.compose.waiter.waitForSelectedAppsToUpdate
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
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

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    application = HiltTestApplication::class,
    instrumentedPackages = ["androidx.loader.content"]
)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@Ignore
class EditFavoritesScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
        val favoriteNodes = onAllNodesWithTag(testTag = TestTags.TAG_FAVORITES_LIST_ITEM)
        TestApps.all.forEach {
            favoriteNodes.filterToOne(matcher = hasText(text = it.displayName)).assertSelectedApp(
                selectedApp = it.toSelectedAppWith(isSelected = false)
            )
        }
    }

    @Test
    fun `2 - when all apps are added to favorite, every item in the list must be selected`() = with(composeTestRule) {
        val favoriteNodes = onAllNodesWithTag(testTag = TestTags.TAG_FAVORITES_LIST_ITEM)
        favoriteNodes.fetchSemanticsNodes().forEachIndexed { index, _ ->
            favoriteNodes[index].performClick()
        }

        favoriteNodes.waitForSelectedAppsToUpdate(
            selectedApps = TestApps.all.toSelectedAppWith(isSelected = true)
        )

        TestApps.all.forEach { app ->
            favoriteNodes.filter(matcher = hasText(text = app.displayName)).onFirst().assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = true)
            )
        }
    }

    @Test
    fun `3 - when favorites are cleared, every item in the list must not be selected`() = with(composeTestRule) {
        val favoriteNodes = onAllNodesWithTag(testTag = TestTags.TAG_FAVORITES_LIST_ITEM)
        favoriteNodes.fetchSemanticsNodes().forEachIndexed { index, _ ->
            favoriteNodes[index].performClick()
        }

        favoriteNodes.waitForSelectedAppsToUpdate(
            selectedApps = TestApps.all.toSelectedAppWith(isSelected = true)
        )

        TestApps.all.forEach { app ->
            favoriteNodes.filter(hasText(app.displayName)).onFirst().assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = true)
            )
        }

        onNodeWithTag(testTag = TestTags.TAG_CLEAR_FAVORITES_FAB).performClick()

        favoriteNodes.waitForSelectedAppsToUpdate(
            selectedApps = TestApps.all.toSelectedAppWith(isSelected = false)
        )

        TestApps.all.forEach { app ->
            favoriteNodes.filter(matcher = hasText(text = app.displayName)).onFirst().assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = false)
            )
        }
    }

    @Test
    fun `4 - when hidden apps are not shown & an app is hidden, it should not be listed in favorites`() = with(composeTestRule) {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val apps = TestApps.all - hiddenApps.toSet()
        runBlocking {
            hiddenAppsRepo.addToHiddenApps(apps = hiddenApps)
        }

        val favoriteNodes = onAllNodesWithTag(testTag = TestTags.TAG_FAVORITES_LIST_ITEM)
        favoriteNodes.fetchSemanticsNodes().forEachIndexed { index, _ ->
            favoriteNodes[index].performClick()
        }

        favoriteNodes.waitForSelectedAppsToUpdate(
            selectedApps = apps.toSelectedAppWith(isSelected = true)
        )

        apps.forEach { app ->
            favoriteNodes.filter(matcher = hasText(text = app.displayName)).onFirst().assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = true)
            )
        }
    }

    @Test
    fun `5 - when hidden apps are shown & an app is hidden, it should be listed in favorites as disabled`() = with(composeTestRule) {
        val hiddenApps = listOf(TestApps.Phone, TestApps.Chrome)
        val apps = TestApps.all
        runBlocking {
            hiddenAppsRepo.addToHiddenApps(apps = hiddenApps)
        }
        viewModel.shouldShowHiddenAppsInFavorites(value = true)

        val favoriteNodes = onAllNodesWithTag(testTag = TestTags.TAG_FAVORITES_LIST_ITEM)
        favoriteNodes.fetchSemanticsNodes().forEachIndexed { index, _ ->
            favoriteNodes[index].performClick()
        }

        favoriteNodes.waitForSelectedAppsToUpdate(
            selectedApps = apps.toSelectedAppWith(isSelected = true, disabled = true)
        )

        apps.forEach { app ->
            favoriteNodes.filter(matcher = hasText(text = app.displayName)).onFirst().assertSelectedApp(
                selectedApp = app.toSelectedAppWith(isSelected = true, disabled = true)
            )
        }
    }
}

private fun App.toSelectedAppWith(
    isSelected: Boolean = false,
    disabled: Boolean = false
): SelectedApp = SelectedApp(app = this, isSelected = isSelected, disabled = disabled)

private fun List<App>.toSelectedAppWith(
    isSelected: Boolean = false,
    disabled: Boolean = false
): List<SelectedApp> = map { it.toSelectedAppWith(isSelected = isSelected, disabled = disabled) }
