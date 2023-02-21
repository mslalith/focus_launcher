package dev.mslalith.focuslauncher.feature.settingspage.shared

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun LoadingSettingsItem(
    text: String,
    isLoading: Boolean,
    horizontalPadding: Dp? = null,
    afterLeadingPadding: Dp = 20.dp,
    onClick: (() -> Unit)? = null
) {
    val density = LocalDensity.current
    var contentHeight by remember { mutableStateOf(0.dp) }

    SettingsItem(
        text = text,
        horizontalPadding = horizontalPadding,
        afterLeadingPadding = afterLeadingPadding,
        onClick = if (isLoading) null else onClick,
        leading = if (isLoading) {
            {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onBackground,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(size = contentHeight / 2)
                )
            }
        } else {
            null
        },
        modifier = Modifier.onSizeChanged {
            contentHeight = density.run { it.height.toDp() }
        }
    )
}
