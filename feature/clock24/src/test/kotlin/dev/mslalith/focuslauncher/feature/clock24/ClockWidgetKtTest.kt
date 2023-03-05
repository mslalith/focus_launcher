package dev.mslalith.focuslauncher.feature.clock24

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.testing.compose.assertion.assertBiasAlignment
import dev.mslalith.focuslauncher.feature.clock24.model.Clock24State
import dev.mslalith.focuslauncher.feature.clock24.utils.TestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class ClockWidgetKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `when clock24 is enabled, clock24 UI must be shown`() {
        composeTestRule.setContent {
            ClockWidget(
                clock24State = stateWith(showClock24 = true),
                refreshTime = {},
                horizontalPadding = 2.dp,
            )
        }

        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_CLOCK24).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).assertDoesNotExist()
    }

    @Test
    fun `when clock24 is disabled, clock24 UI must not be shown`() {
        composeTestRule.setContent {
            ClockWidget(
                clock24State = stateWith(showClock24 = false),
                refreshTime = {},
                horizontalPadding = 2.dp,
            )
        }

        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).assertIsDisplayed()
        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_CLOCK24).assertDoesNotExist()
    }

    @Test
    fun `when clock24 alignment is updated, verify it's placement`() {
        val clock24State = mutableStateOf(value = stateWith(showClock24 = true, clockAlignment = ClockAlignment.START))
        composeTestRule.setContent {
            ClockWidget(
                clock24State = clock24State.value,
                refreshTime = {},
                horizontalPadding = 2.dp,
            )
        }

        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_CLOCK_COLUMN).assertBiasAlignment(BiasAlignment.Horizontal(bias = -1f))

        clock24State.value = clock24State.value.copy(clockAlignment = ClockAlignment.CENTER)
        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_CLOCK_COLUMN).assertBiasAlignment(BiasAlignment.Horizontal(bias = 0f))

        clock24State.value = clock24State.value.copy(clockAlignment = ClockAlignment.END)
        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_CLOCK_COLUMN).assertBiasAlignment(BiasAlignment.Horizontal(bias = 1f))
    }
}

private fun stateWith(
    currentTime: String = "00:00",
    showClock24: Boolean = true,
    clockAlignment: ClockAlignment = ClockAlignment.START,
    clock24AnimationDuration: Int = 1200
) = Clock24State(
    currentTime = currentTime,
    showClock24 = showClock24,
    clockAlignment = clockAlignment,
    clock24AnimationDuration = clock24AnimationDuration
)
