package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.mslalith.focuslauncher.core.model.App

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateAppDisplayNameDialog(
    app: App,
    onUpdateDisplayName: (String) -> Unit,
    onClose: () -> Unit
) {
    var displayName by remember { mutableStateOf(app.displayName) }

    fun onUpdateClick() {
        onUpdateDisplayName(displayName)
        onClose()
    }

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(text = "Update Display Name")
        },
        text = {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                maxLines = 1,
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = ::onUpdateClick
            ) {
                Text(text = "Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text(text = "Cancel")
            }
        }
    )
}
