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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.core.ui.DotWaveLoader
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.screens.currentplace.CurrentPlaceState
import dev.mslalith.focuslauncher.screens.currentplace.R

@Composable
internal fun CurrentPlaceInfo(
    currentPlaceState: CurrentPlaceState
) {
    Column(
        modifier = Modifier.animateContentSize()
    ) {
        LatLngInfo(latLng = currentPlaceState.latLng)
        VerticalSpacer(spacing = 8.dp)
        AddressInfo(addressState = currentPlaceState.addressState)
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
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_map_pin),
            contentDescription = stringResource(id = R.string.location_icon),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
                .padding(start = 4.dp, end = 12.dp)
        )
        Crossfade(
            label = "Cross Fade Lat Long",
            targetState = latLng
        ) {
            Text(
                text = "${it.latitude},  ${it.longitude}",
                color = MaterialTheme.colorScheme.onPrimaryContainer
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
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_house),
            contentDescription = stringResource(id = R.string.address_icon),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
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
                            color = MaterialTheme.colorScheme.onPrimaryContainer
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
