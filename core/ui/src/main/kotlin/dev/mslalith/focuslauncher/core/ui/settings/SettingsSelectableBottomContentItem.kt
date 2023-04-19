package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSelectableBottomContentItem(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    disabled: Boolean = false,
    backgroundColor: Color = Color.Transparent,
    horizontalPadding: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    var showBottomContent by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = disabled) {
        if (disabled) {
            showBottomContent = false
        }
    }

    Column {
        val contentColor = MaterialTheme.colorScheme.onSurface
        val animatedContentColor by animateColorAsState(
            label = "Background color",
            targetValue = if (disabled) contentColor.copy(alpha = 0.38f) else contentColor
        )

        ListItem(
            modifier = modifier.clickable { showBottomContent = !showBottomContent },
            colors = ListItemDefaults.colors(
                containerColor = backgroundColor,
                headlineColor = animatedContentColor
            ),
            headlineText = { Text(text = text) },
            supportingText = { Text(text = subText) }
        )
        AnimatedVisibility(visible = showBottomContent) {
            Box(
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                content()
            }
        }
    }
}
