package dev.mslalith.focuslauncher.screens.currentplace.ui.interop

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import dev.mslalith.focuslauncher.core.common.extensions.limitDecimals
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.screens.currentplace.R
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@Composable
internal fun AndroidMapView(
    modifier: Modifier = Modifier,
    initialLatLng: LatLng,
    onLocationChange: (LatLng) -> Unit
) {
    var currentPositionMarker: Marker? by remember { mutableStateOf(value = null) }
    var mapView: MapView? by remember { mutableStateOf(value = null) }

    fun MapView.updateCurrentPositionMarker(geoPoint: GeoPoint) {
        if (currentPositionMarker == null) {
            currentPositionMarker = Marker(this).apply {
                icon = ContextCompat.getDrawable(context, R.drawable.ic_map_pin_line)
                setInfoWindow(null)
            }
            overlays.add(currentPositionMarker)
        }
        currentPositionMarker?.apply {
            position = geoPoint
            controller.animateTo(geoPoint)
            onLocationChange(geoPoint.toLatLng(limitDecimals = 5))
        }
    }

    LaunchedEffect(key1 = initialLatLng) {
        val geoPoint = GeoPoint(initialLatLng.latitude, initialLatLng.longitude)
        mapView?.controller?.animateTo(geoPoint)
        mapView?.controller?.setZoom(7.0)
        mapView?.updateCurrentPositionMarker(geoPoint = geoPoint)
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
            }.also { mapView = it }
        },
        onReset = { it.onResume() },
        onRelease = { it.onPause() },
        update = {}
    )
}

@IgnoreInKoverReport
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

@IgnoreInKoverReport
private fun GeoPoint.toLatLng(limitDecimals: Int): LatLng = LatLng(
    latitude = latitude.limitDecimals(maxFractions = limitDecimals).toDouble(),
    longitude = longitude.limitDecimals(maxFractions = limitDecimals).toDouble()
)
