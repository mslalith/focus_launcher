package dev.mslalith.focuslauncher.feature.clock24

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.feature.clock24.utils.TestTags

@Composable
internal fun CurrentTime(
    modifier: Modifier = Modifier,
    currentTime: String,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier.testSemantics(tag = TestTags.TAG_REGULAR_CLOCK)
    ) {
        Crossfade(
            label = "Current Time CrossFade",
            targetState = currentTime
        ) {
            Text(
                text = it,
                color = contentColor,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}
