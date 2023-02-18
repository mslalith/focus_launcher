package dev.mslalith.focuslauncher.ui.views

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.data.models.ConfirmDialogProperties

@Composable
fun ConfirmDialog(
    properties: ConfirmDialogProperties
) {
    properties.apply {
        AlertDialog(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            contentColor = MaterialTheme.colors.onBackground,
            title = { Text(title) },
            text = { Text(message) },
            onDismissRequest = onCancel,
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp)
                ) {
                    Spacer(Modifier.weight(1f))
                    DialogActionButton(
                        text = cancelButtonText,
                        onClick = onCancel
                    )
                    Spacer(Modifier.width(8.dp))
                    DialogActionButton(
                        text = confirmButtonText,
                        onClick = onConfirm
                    )
                }
            }
        )
    }
}

@Composable
private fun DialogActionButton(
    text: String,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit
) {
    TextButton(
        text = text,
        backgroundColor = backgroundColor,
        textColor = contentColor,
        paddingValues = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
        onClick = onClick
    )
}
