package dev.mslalith.focuslauncher.feature.settingspage

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dev.mslalith.focuslauncher.core.model.AppDrawerViewType
import dev.mslalith.focuslauncher.core.model.R
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerIconViewType
import dev.mslalith.focuslauncher.core.testing.compose.assertion.assertBooleanType
import dev.mslalith.focuslauncher.feature.settingspage.model.SettingsSheetState
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags
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
class AppDrawerSettingsSheetKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var state: SettingsSheetState by mutableStateOf(value = stateWith())

    @Before
    fun setup() {
        state = stateWith()
        composeTestRule.initializeWith()
    }

    @Test
    fun `01 - verify initial state`(): Unit = with(composeTestRule) {
        onNodeWithTag(testTag = TestTags.SHEET_APPS_VIEW_TYPE).assertSettingsValue(value = activity.getString(R.string.list))
        onNodeWithTag(testTag = TestTags.SHEET_SHOW_SEARCH_BAR).assertBooleanType(value = true)
        onNodeWithTag(testTag = TestTags.SHEET_SHOW_APP_GROUP_HEADER).assertBooleanType(value = true)
    }

    @Test
    fun `02 - when new apps view type is selected, the UI must update with new value`(): Unit = with(composeTestRule) {
        val gridText = activity.getString(R.string.grid)
        val listText = activity.getString(R.string.list)

        onNodeWithTag(testTag = TestTags.SHEET_APPS_VIEW_TYPE).apply {
            assertSettingsValue(value = listText)
            updateAppsViewType(appsViewType = AppDrawerViewType.GRID)
            assertSettingsValue(value = gridText)
        }
    }

    @Test
    fun `03 - when search bar his hidden, it's option should be disabled`(): Unit = with(composeTestRule) {
        onNodeWithTag(testTag = TestTags.SHEET_SHOW_SEARCH_BAR).apply {
            performClick()
            assertBooleanType(value = false)
        }
    }

    @Test
    fun `04 - when group apps by character is disabled, it's option should be disabled`(): Unit = with(composeTestRule) {
        onNodeWithTag(testTag = TestTags.SHEET_SHOW_APP_GROUP_HEADER).apply {
            performClick()
            assertBooleanType(value = false)
        }
    }

    @Test
    fun `05 - when apps view type is grid, group apps by character must be disabled`(): Unit = with(composeTestRule) {
        onNodeWithTag(testTag = TestTags.SHEET_SHOW_APP_GROUP_HEADER).apply {
            hasAnyChild(matcher = isEnabled())
            updateAppsViewType(appsViewType = AppDrawerViewType.GRID)
            hasAnyChild(matcher = isNotEnabled())
        }
    }

    @Test
    fun `06 - when new app icon type is selected, the UI must update with new value`(): Unit = with(composeTestRule) {
        val iconsText = activity.getString(R.string.icons)
        val coloredText = activity.getString(R.string.colored)

        onNodeWithTag(testTag = TestTags.SHEET_APP_ICON_TYPE).apply {
            assertSettingsValue(value = iconsText)
            updateAppIconViewType(iconViewType = AppDrawerIconViewType.COLORED)
            assertSettingsValue(value = coloredText)
        }
    }

    private fun AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>.initializeWith() {
        setContent {
            AppDrawerSettingsSheetInternal(
                settingsSheetState = state,
                onUpdateAppDrawerViewType = { state = state.copy(appDrawerViewType = it) },
                onUpdateAppDrawerIconViewType = { state = state.copy(appDrawerIconViewType = it) },
                onToggleSearchBarVisibility = { state = state.copy(showSearchBar = !state.showSearchBar) },
                onToggleAppGroupHeaderVisibility = { state = state.copy(showAppGroupHeader = !state.showAppGroupHeader) }
            )
        }
    }
}

private fun stateWith(): SettingsSheetState = SettingsSheetState(
    appDrawerViewType = AppDrawerViewType.LIST,
    appDrawerIconViewType = AppDrawerIconViewType.ICONS,
    showAppGroupHeader = true,
    showSearchBar = true
)

private fun AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>.updateAppsViewType(
    appsViewType: AppDrawerViewType
) {
    val viewTypeItem = onNodeWithTag(testTag = TestTags.SHEET_APPS_VIEW_TYPE)
    val chooserGroup = onNodeWithTag(testTag = TestTags.SHEET_APPS_VIEW_TYPE_CHOOSER_GROUP)

    viewTypeItem.performClick()
    chooserGroup.onChildren().filterToOne(
        matcher = hasTestTag(
            testTag = appsViewType.uiText.string(context = activity)
        )
    ).performClick()
}

private fun AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>.updateAppIconViewType(
    iconViewType: AppDrawerIconViewType
) {
    val viewTypeItem = onNodeWithTag(testTag = TestTags.SHEET_APP_ICON_TYPE)
    val chooserGroup = onNodeWithTag(testTag = TestTags.SHEET_APP_ICON_TYPE_CHOOSER_GROUP)

    viewTypeItem.performClick()
    chooserGroup.onChildren().filterToOne(
        matcher = hasTestTag(
            testTag = iconViewType.uiText.string(context = activity)
        )
    ).performClick()
}

private fun SemanticsNodeInteraction.assertSettingsValue(value: String) {
    onChildren().assertAny(matcher = hasText(text = value))
}
