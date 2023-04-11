package dev.mslalith.focuslauncher.screens.currentplace.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.model.CurrentPlace
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.DotWaveLoader
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer

@Composable
internal fun CurrentPlaceInfo(
    currentPlace: CurrentPlace,
    addressState: LoadingState<String>
) {
    Column(
        modifier = Modifier.animateContentSize(),
    ) {
        LatLngInfo(latLng = currentPlace.latLng)
        VerticalSpacer(spacing = 8.dp)
        AddressInfo(addressState = addressState)
    }
}

@Composable
private fun LatLngInfo(
    latLng: LatLng
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant)
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.LocationOn,
            contentDescription = "Location icon",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp, end = 12.dp)
        )
        Column {
            Text(
                text = "Latitude: ${latLng.latitude}",
                color = MaterialTheme.colors.onBackground
            )
            VerticalSpacer(spacing = 4.dp)
            Text(
                text = "Longitude: ${latLng.longitude}",
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun AddressInfo(
    addressState: LoadingState<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Home,
            contentDescription = "Address icon",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp, end = 12.dp)
        )
        Crossfade(
            label = "Address Info Cross Fade",
            targetState = addressState
        ) {
            when (it) {
                is LoadingState.Loaded -> {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it.value,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
                LoadingState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        DotWaveLoader()
                    }
                }
            }
        }
    }
}
