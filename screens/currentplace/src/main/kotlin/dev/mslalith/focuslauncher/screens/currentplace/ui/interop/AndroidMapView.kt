package dev.mslalith.focuslauncher.screens.currentplace.ui.interop

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import dev.mslalith.focuslauncher.core.common.extensions.limitDecimals
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.screens.currentplace.R
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AndroidMapView(
    modifier: Modifier = Modifier,
    initialLatLngProvider: suspend () -> LatLng,
    onLocationChange: (LatLng) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var currentPositionMarker: Marker? = remember { null }

    fun MapView.updateCurrentPositionMarker(geoPoint: GeoPoint) {
        if (currentPositionMarker == null) {
            currentPositionMarker = Marker(this).apply {
                icon = ContextCompat.getDrawable(context, R.drawable.ic_location_on)
            }
            overlays.add(currentPositionMarker)
        }
        currentPositionMarker?.apply {
            position = geoPoint
            controller.animateTo(geoPoint)
            onLocationChange(geoPoint.toLatLng(limitDecimals = 5))
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            Configuration.getInstance().load(context.applicationContext, context.getSharedPreferences("focus_launcher_map", Context.MODE_PRIVATE))
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                addOnTapListener { updateCurrentPositionMarker(geoPoint = it) }
                coroutineScope.launch {
                    val initialLatLng = initialLatLngProvider()
                    val geoPoint = GeoPoint(initialLatLng.latitude, initialLatLng.longitude)
                    controller.animateTo(geoPoint)
                    controller.setZoom(7.0)
                    updateCurrentPositionMarker(geoPoint)
                }
            }
        },
        onReset = { it.onResume() },
        onRelease = { it.onPause() },
        update = {}
    )
}

private fun MapView.addOnTapListener(block: (GeoPoint) -> Unit) {
    val overlay = MapEventsOverlay(object : MapEventsReceiver {
        override fun singleTapConfirmedHelper(geoPoint: GeoPoint?): Boolean {
            if (geoPoint != null) block(geoPoint)
            return true
        }

        override fun longPressHelper(p: GeoPoint?): Boolean {
            return false
        }
    })
    overlays.add(overlay)
}

private fun GeoPoint.toLatLng(limitDecimals: Int): LatLng = LatLng(
    latitude = latitude.limitDecimals(precision = limitDecimals).toDouble(),
    longitude = longitude.limitDecimals(precision = limitDecimals).toDouble()
)
