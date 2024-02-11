package dev.mslalith.focuslauncher.screens.developer

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.DeveloperScreen
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon

@CircuitInject(DeveloperScreen::class, SingletonComponent::class)
@Composable
fun Developer(
    state: DeveloperState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    val openFileLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
        it?.let { eventSink(DeveloperUiEvent.ReadFavoritesFromUri(uri = it)) }
    }
    val createFileLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument(mimeType = "application/json")) {
        it?.let { eventSink(DeveloperUiEvent.SaveFavoritesFromUri(uri = it)) }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AppBarWithBackIcon(
                title = stringResource(id = R.string.developer),
                onBackPressed = { eventSink(DeveloperUiEvent.GoBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.read_favorites)) },
                trailingContent = if (state.isFavoritesReading) {
                    { LoadingIndicator() }
                } else null,
                modifier = Modifier.clickable { openFileLauncher.launch(arrayOf("application/json")) }
            )
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.save_favorites)) },
                trailingContent = if (state.isFavoritesSaving) {
                    { LoadingIndicator() }
                } else null,
                modifier = Modifier.clickable { createFileLauncher.launch(state.defaultFavoritesName) }
            )
        }
    }
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
