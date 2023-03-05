package dev.mslalith.focuslauncher.feature.clock24

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import dev.mslalith.focuslauncher.feature.clock24.utils.TestTags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class CurrentTimeKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun `verify initial time`() {
        composeTestRule.setContent { 
            CurrentTime(currentTime = "17:23")
        }

        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).onChildren().assertAny(hasText("17:23"))
    }

    @Test
    fun `when time is updated, verify time`() {
        val currentTime = mutableStateOf("17:23")
        composeTestRule.setContent {
            CurrentTime(currentTime = currentTime.value)
        }

        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).onChildren().assertAny(hasText("17:23"))

        currentTime.value = "21:49"
        composeTestRule.onNodeWithTag(testTag = TestTags.TAG_REGULAR_CLOCK).onChildren().assertAny(hasText("21:49"))
    }
}
