package dev.mslalith.focuslauncher.screens.currentplace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.screens.currentplace.ui.CurrentPlaceInfo
import dev.mslalith.focuslauncher.screens.currentplace.ui.interop.AndroidMapView
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
        currentPlace = currentPlaceViewModel.currentPlaceStateFlow.collectAsState().value,
        goBack = goBack,
        initialLatLngProvider = { currentPlaceViewModel.fetchCurrentPlaceFromDb().latLng },
        onDoneClick = ::onDoneClick,
        onLocationChange = currentPlaceViewModel::updateCurrentLocation
    )
}

@Composable
internal fun CurrentPlaceScreen(
    currentPlace: CurrentPlace,
    goBack: () -> Unit,
    initialLatLngProvider: suspend () -> LatLng,
    onDoneClick: () -> Unit,
    onLocationChange: (LatLng) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackIcon(
                title = "Update Place",
                onBackPressed = goBack,
                actions = {
                    IconButton(onClick = onDoneClick) {
                        Icon(
                            Icons.Rounded.Done,
                            contentDescription = "Done icon",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
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
            CurrentPlaceInfo(currentPlace = currentPlace)
            VerticalSpacer(spacing = 16.dp)
            AndroidMapView(
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
fun PreviewUpdatePlace() {
    MaterialTheme {
        CurrentPlaceScreen(
            currentPlace = CurrentPlace(LatLng(0.0, 0.0), "Not Available"),
            goBack = { },
            initialLatLngProvider = { LatLng(0.0, 0.0) },
            onDoneClick = { },
            onLocationChange = { }
        )
    }
}
