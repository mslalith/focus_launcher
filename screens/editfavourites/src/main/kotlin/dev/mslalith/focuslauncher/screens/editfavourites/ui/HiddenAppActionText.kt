package dev.mslalith.focuslauncher.screens.editfavourites.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colors.onBackground
        ),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = " Hidden Apps ",
            style = TextStyle(
                fontSize = 12.sp,
                textDecoration = textDecoration
            )
        )
    }
}
