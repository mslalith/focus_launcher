package dev.mslalith.focuslauncher.screens.currentplace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.screens.currentplace.model.CurrentPlaceState
import dev.mslalith.focuslauncher.screens.currentplace.ui.CurrentPlaceInfo
import dev.mslalith.focuslauncher.screens.currentplace.ui.MapView
import kotlinx.coroutines.launch

@Composable
fun CurrentPlaceScreen(
    goBack: () -> Unit
) {
    CurrentPlaceScreen(
        currentPlaceViewModel = hiltViewModel(),
        goBack = goBack
    )
}

@Composable
internal fun CurrentPlaceScreen(
    currentPlaceViewModel: CurrentPlaceViewModel,
    goBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    fun onDoneClick() {
        coroutineScope.launch {
            currentPlaceViewModel.savePlace()
            goBack()
        }
    }

    CurrentPlaceScreen(
        currentPlaceState = currentPlaceViewModel.currentPlaceState.collectAsStateWithLifecycle().value,
        goBack = goBack,
        initialLatLngProvider = { currentPlaceViewModel.fetchCurrentPlaceFromDb().latLng },
        onDoneClick = ::onDoneClick,
        onLocationChange = currentPlaceViewModel::updateCurrentLatLng
    )
}

@Composable
internal fun CurrentPlaceScreen(
    currentPlaceState: CurrentPlaceState,
    goBack: () -> Unit,
    initialLatLngProvider: suspend () -> LatLng,
    onDoneClick: () -> Unit,
    onLocationChange: (LatLng) -> Unit
) {
    Scaffold(
        topBar = {
            AppBarWithBackIcon(
                title = "Update Place",
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
                initialLatLngProvider = initialLatLngProvider,
                onLocationChange = onLocationChange
            )
            VerticalSpacer(spacing = 8.dp)
        }
    }
}

@Preview
@Composable
private fun PreviewUpdatePlace() {
    MaterialTheme {
        CurrentPlaceScreen(
            currentPlaceState = CurrentPlaceState(
                latLng = LatLng(latitude = 0.0, longitude = 0.0),
                addressState = LoadingState.Loading,
                canSave = false
            ),
            goBack = { },
            initialLatLngProvider = { LatLng(latitude = 0.0, longitude = 0.0) },
            onDoneClick = { },
            onLocationChange = { }
        )
    }
}
