package dev.mslalith.focuslauncher.core.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
        trailingContent = if (isLoading) {
            @Composable { LoadingIndicator() }
        } else null
    )
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.size(size = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(strokeWidth = 2.dp)
    }
}
