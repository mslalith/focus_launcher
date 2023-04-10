package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsLoadableItem(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    subText: String? = null,
    disabled: Boolean = false,
    onClick: (() -> Unit)?
) {
    SettingsSelectableItem(
        modifier = modifier,
        text = text,
        subText = subText,
        disabled = disabled,
        onClick = { if (!isLoading) onClick?.invoke() },
        trailing = if (isLoading) {
            @Composable { LoadingIndicator() }
        } else {
            @Composable {}
        }
    )
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.5f)
            .aspectRatio(ratio = 1f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onBackground,
            strokeWidth = 2.dp
        )
    }
}
