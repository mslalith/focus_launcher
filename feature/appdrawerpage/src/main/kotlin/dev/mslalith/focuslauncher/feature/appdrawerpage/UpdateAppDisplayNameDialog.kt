package dev.mslalith.focuslauncher.feature.appdrawerpage

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.App

@Composable
internal fun UpdateAppDisplayNameDialog(
    app: App,
    onUpdateDisplayName: (String) -> Unit,
    onClose: () -> Unit
) {
    var displayName by remember { mutableStateOf(value = app.displayName) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    fun onUpdateClick() {
        onUpdateDisplayName(displayName)
        onClose()
    }

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(text = stringResource(id = R.string.update_display_name))
        },
        text = {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.focusRequester(focusRequester = focusRequester)
            )
        },
        confirmButton = {
            Button(onClick = ::onUpdateClick) {
                Text(text = stringResource(id = R.string.update))
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}
