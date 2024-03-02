package dev.mslalith.focuslauncher.screens.currentplace

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.model.LoadingState
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

            AnimatedVisibility(visible = !currentPlaceState.isOnline) {
                Column {
                    Text(text = stringResource(id = R.string.this_feature_requires_internet_access))
                    VerticalSpacer(spacing = 16.dp)
                }
            }

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

@Preview
@Composable
private fun PreviewCurrentPlaceScreen() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                VerticalSpacer(spacing = 8.dp)
                AnimatedVisibility(visible = true) {
                    Text(text = stringResource(id = R.string.this_feature_requires_internet_access))
                    VerticalSpacer(spacing = 16.dp)
                }

                VerticalSpacer(spacing = 16.dp)
                CurrentPlaceInfo(
                    currentPlaceState = CurrentPlaceState(
                        latLng = LatLng(latitude = 0.0, longitude = 0.0),
                        initialLatLng = LatLng(latitude = 0.0, longitude = 0.0),
                        addressState = LoadingState.Loaded(value = "Soul Buoy"),
                        isOnline = false,
                        canSave = true,
                        eventSink = {}
                    )
                )
                VerticalSpacer(spacing = 8.dp)
            }
        }
    }
}
