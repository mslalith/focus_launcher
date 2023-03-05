package dev.mslalith.focuslauncher.feature.clock24

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.feature.clock24.utils.TestTags

@Composable
internal fun CurrentTime(
    modifier: Modifier = Modifier,
    currentTime: String,
    centerVertically: Boolean = false
) {
    val newModifier = if (centerVertically) modifier.fillMaxHeight() else modifier

    Box(
        modifier = newModifier.testSemantics(tag = TestTags.TAG_REGULAR_CLOCK),
        contentAlignment = if (centerVertically) Alignment.Center else Alignment.TopStart
    ) {
        Crossfade(
            label = "Current Time CrossFade",
            targetState = currentTime
        ) {
            Text(
                text = it,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
