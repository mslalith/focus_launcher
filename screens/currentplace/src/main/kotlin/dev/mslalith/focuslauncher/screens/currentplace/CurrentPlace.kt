package dev.mslalith.focuslauncher.screens.currentplace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.screens.CurrentPlaceScreen
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.screens.currentplace.ui.CurrentPlaceInfo
import dev.mslalith.focuslauncher.screens.currentplace.ui.MapView

@CircuitInject(CurrentPlaceScreen::class, SingletonComponent::class)
@Composable
fun CurrentPlace(
    state: CurrentPlaceState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    CurrentPlaceScreen(
        modifier = modifier,
        currentPlaceState = state,
        goBack = { eventSink(CurrentPlaceUiEvent.GoBack) },
        onDoneClick = { eventSink(CurrentPlaceUiEvent.SavePlace) },
        onLocationChange = { eventSink(CurrentPlaceUiEvent.UpdateCurrentLatLng(latLng = it)) }
    )
}

@Composable
private fun CurrentPlaceScreen(
    currentPlaceState: CurrentPlaceState,
    goBack: () -> Unit,
    onDoneClick: () -> Unit,
    onLocationChange: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            AppBarWithBackIcon(
                title = stringResource(id = R.string.update_place),
                onBackPressed = goBack,
                actions = {
                    RoundIcon(
                        iconRes = R.drawable.ic_check,
                        enabled = currentPlaceState.canSave,
                        onClick = onDoneClick
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            VerticalSpacer(spacing = 8.dp)
            CurrentPlaceInfo(currentPlaceState = currentPlaceState)
            VerticalSpacer(spacing = 16.dp)
            MapView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f)
                    .clip(shape = MaterialTheme.shapes.small),
                initialLatLng = currentPlaceState.initialLatLng,
                onLocationChange = onLocationChange
            )
            VerticalSpacer(spacing = 8.dp)
        }
    }
}
