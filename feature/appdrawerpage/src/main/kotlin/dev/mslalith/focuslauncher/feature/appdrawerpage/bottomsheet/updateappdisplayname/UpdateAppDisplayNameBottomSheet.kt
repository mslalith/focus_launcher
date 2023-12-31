package dev.mslalith.focuslauncher.feature.appdrawerpage.bottomsheet.updateappdisplayname

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.screens.UpdateAppDisplayNameBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.feature.appdrawerpage.R

@CircuitInject(UpdateAppDisplayNameBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun UpdateAppDisplayNameBottomSheet(
    state: UpdateAppDisplayNameBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    UpdateAppDisplayNameBottomSheet(
        modifier = modifier,
        app = state.app,
        onUpdateClick = { eventSink(UpdateAppDisplayNameBottomSheetUiEvent.UpdateDisplayName(displayName = it)) }
    )
}

@Composable
private fun UpdateAppDisplayNameBottomSheet(
    app: App,
    onUpdateClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var displayName by remember {
        mutableStateOf(
            value = TextFieldValue(
                text = app.displayName,
                selection = TextRange(index = app.displayName.length)
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        VerticalSpacer(spacing = 16.dp)
        Text(
            text = stringResource(id = R.string.update_display_name),
            style = MaterialTheme.typography.headlineSmall
        )

        VerticalSpacer(spacing = 16.dp)
        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = focusRequester)
        )

        VerticalSpacer(spacing = 16.dp)
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onUpdateClick(displayName.text) }
        ) {
            Text(
                text = stringResource(id = R.string.update),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        VerticalSpacer(spacing = 16.dp)
    }
}

@Preview
@Composable
private fun PreviewUpdateAppDisplayNameBottomSheet() {
    MaterialTheme {
        Surface {
            UpdateAppDisplayNameBottomSheet(
                app = App(name = "Chrome", displayName = "Chrome", packageName = "com.chrome", isSystem = true),
                onUpdateClick = {}
            )
        }
    }
}
