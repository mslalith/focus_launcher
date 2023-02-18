package dev.mslalith.focuslauncher.ui.views.dialogs

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.mslalith.focuslauncher.data.model.App

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAppDisplayNameDialog(
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
        containerColor = MaterialTheme.colors.primaryVariant,
        titleContentColor = MaterialTheme.colors.onBackground,
        textContentColor = MaterialTheme.colors.onBackground,
        title = {
            Text(
                text = "Update Display Name"
            )
        },
        text = {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                maxLines = 1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.onBackground,
                    focusedBorderColor = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                    unfocusedBorderColor = MaterialTheme.colors.secondaryVariant
                )
            )
        },
        confirmButton = {
            Button(
                onClick = ::onUpdateClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colors.secondaryVariant,
                    contentColor = MaterialTheme.colors.onBackground
                )
            ) {
                Text(text = "Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    )
}
