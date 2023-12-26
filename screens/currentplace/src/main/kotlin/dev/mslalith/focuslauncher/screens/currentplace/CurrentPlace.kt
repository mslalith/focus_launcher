package dev.mslalith.focuslauncher.screens.currentplace

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.screens.CurrentPlaceScreen

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
        currentPlaceState = dev.mslalith.focuslauncher.screens.currentplace.model.CurrentPlaceState(
            state.latLng,
            state.initialLatLng,
            state.addressState,
            state.canSave
        ),
        goBack = { eventSink(CurrentPlaceUiEvent.GoBack) },
        onDoneClick = { eventSink(CurrentPlaceUiEvent.SavePlace) },
        onLocationChange = { eventSink(CurrentPlaceUiEvent.UpdateCurrentLatLng(latLng = it)) }
    )
}
