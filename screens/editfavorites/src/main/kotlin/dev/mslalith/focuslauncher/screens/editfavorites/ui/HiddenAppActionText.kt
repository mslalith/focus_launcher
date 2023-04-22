package dev.mslalith.focuslauncher.screens.editfavorites.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
internal fun HiddenAppActionText(
    showHiddenApps: Boolean,
    onToggleHiddenApps: (Boolean) -> Unit
) {
    val textDecoration = when (showHiddenApps) {
        true -> TextDecoration.None
        false -> TextDecoration.LineThrough
    }

    TextButton(
        onClick = { onToggleHiddenApps(!showHiddenApps) },
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = " Hidden Apps ",
            textDecoration = textDecoration,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
