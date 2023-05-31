package dev.mslalith.focuslauncher.feature.clock24

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import dev.mslalith.focuslauncher.feature.clock24.utils.TestTags
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
class CurrentTimeKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `01 - verify initial time`(): Unit = with(composeTestRule) {
        setContent {
            CurrentTime(currentTime = "17:23")
        }

        onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).onChildren().assertAny(matcher = hasText(text = "17:23"))
    }

    @Test
    fun `02 - when time is updated, verify time`(): Unit = with(composeTestRule) {
        var currentTime by mutableStateOf("17:23")

        setContent {
            CurrentTime(currentTime = currentTime)
        }

        onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).onChildren().assertAny(matcher = hasText(text = "17:23"))

        currentTime = "21:49"
        onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).onChildren().assertAny(matcher = hasText(text = "21:49"))
    }
}
