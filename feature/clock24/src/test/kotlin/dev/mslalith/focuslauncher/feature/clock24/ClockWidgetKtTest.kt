package dev.mslalith.focuslauncher.feature.clock24

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.ClockAlignment
import dev.mslalith.focuslauncher.core.testing.AppRobolectricTestRunner
import dev.mslalith.focuslauncher.core.testing.compose.assertion.assertBiasAlignment
import dev.mslalith.focuslauncher.feature.clock24.utils.TestTags
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponent
import dev.mslalith.focuslauncher.feature.clock24.widget.ClockWidgetUiComponentState
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.annotation.Config

@RunWith(AppRobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class ClockWidgetKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `01 - when clock24 is enabled, clock24 UI must be shown`(): Unit = with(composeTestRule) {
        setContent {
            ClockWidgetUiComponent(
                state = stateWith(showClock24 = true),
                horizontalPadding = 2.dp
            )
        }

        onNodeWithTag(testTag = TestTags.TAG_CLOCK24).assertIsDisplayed()
        onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).assertDoesNotExist()
    }

    @Test
    fun `02 - when clock24 is disabled, clock24 UI must not be shown`(): Unit = with(composeTestRule) {
        setContent {
            ClockWidgetUiComponent(
                state = stateWith(showClock24 = false),
                horizontalPadding = 2.dp
            )
        }

        onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).assertIsDisplayed()
        onNodeWithTag(testTag = TestTags.TAG_CLOCK24).assertDoesNotExist()
    }

    @Test
    fun `03 - when clock24 alignment is updated, verify it's placement`(): Unit = with(composeTestRule) {
        var clock24State by mutableStateOf(value = stateWith(showClock24 = true, clockAlignment = ClockAlignment.START))
        setContent {
            ClockWidgetUiComponent(
                state = clock24State,
                horizontalPadding = 2.dp
            )
        }

        onNodeWithTag(testTag = TestTags.TAG_CLOCK_COLUMN).assertBiasAlignment(BiasAlignment.Horizontal(bias = -1f))

        clock24State = clock24State.copy(clockAlignment = ClockAlignment.CENTER)
        onNodeWithTag(testTag = TestTags.TAG_CLOCK_COLUMN).assertBiasAlignment(BiasAlignment.Horizontal(bias = 0f))

        clock24State = clock24State.copy(clockAlignment = ClockAlignment.END)
        onNodeWithTag(testTag = TestTags.TAG_CLOCK_COLUMN).assertBiasAlignment(BiasAlignment.Horizontal(bias = 1f))
    }
}

private fun stateWith(
    currentTime: String = "00:00",
    showClock24: Boolean = true,
    use24Hour: Boolean = true,
    clockAlignment: ClockAlignment = ClockAlignment.START,
    clock24AnimationDuration: Int = 1200
) = ClockWidgetUiComponentState(
    currentTime = currentTime,
    showClock24 = showClock24,
    use24Hour = use24Hour,
    clockAlignment = clockAlignment,
    clock24AnimationDuration = clock24AnimationDuration,
    eventSink = {}
)
