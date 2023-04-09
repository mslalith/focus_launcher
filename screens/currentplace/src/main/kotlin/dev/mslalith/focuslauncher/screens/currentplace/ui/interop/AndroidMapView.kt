package dev.mslalith.focuslauncher.screens.currentplace.ui.interop

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.screens.currentplace.R
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AndroidMapView(
    modifier: Modifier = Modifier,
    onLocationChange: (LatLng) -> Unit
) {
    val context = LocalContext.current
    var currentPositionMarker: Marker? = remember { null }

    fun MapView.updateCurrentPositionMarker(geoPoint: IGeoPoint?) {
        geoPoint ?: return
        if (currentPositionMarker == null) {
            currentPositionMarker = Marker(this).apply {
                title = "Here I am"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                icon = ContextCompat.getDrawable(context, R.drawable.ic_location_on)
            }
            overlays.add(currentPositionMarker)
        }
        currentPositionMarker?.apply {
            position = GeoPoint(geoPoint.latitude, geoPoint.longitude)
            onLocationChange(LatLng(latitude = geoPoint.latitude, longitude = geoPoint.longitude))
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            Configuration.getInstance().load(context.applicationContext, context.getSharedPreferences("focus_launcher_map", Context.MODE_PRIVATE))
            MapView(it).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                val geoPoint = GeoPoint(17.45678, 83.5678)
                controller.setCenter(geoPoint)
                controller.setZoom(7.0)
                updateCurrentPositionMarker(geoPoint)
                addMapListener(object : MapListener {
                    override fun onScroll(event: ScrollEvent?): Boolean {
                        updateCurrentPositionMarker(event?.source?.mapCenter)
                        return false
                    }

                    override fun onZoom(event: ZoomEvent?): Boolean {
                        updateCurrentPositionMarker(event?.source?.mapCenter)
                        return false
                    }
                })
            }
        },
        onReset = { it.onResume() },
        onRelease = { it.onPause() },
        update = {}
    )
}
