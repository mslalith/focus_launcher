package dev.mslalith.focuslauncher.ui.views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.mslalith.focuslauncher.data.model.App

@Composable
fun UpdateAppDisplayNameDialog(
    app: App,
    onUpdateDisplayName: (String) -> Unit,
    onClose: () -> Unit,
) {
    Dialog(
        onDismissRequest = onClose
    ) {
        var displayName by remember { mutableStateOf(app.displayName) }

        fun onUpdateClick() {
            onUpdateDisplayName(displayName)
            onClose()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(size = 12.dp))
                .background(color = MaterialTheme.colors.primaryVariant)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Text(text = "Display Name")
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it }
            )
            Row {
                TextButton(onClick = onClose) {
                    Text(text = "Cancel")
                }
                Button(onClick = ::onUpdateClick) {
                    Text(text = "Update")
                }
            }
        }
    }
}
