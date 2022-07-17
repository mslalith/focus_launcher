package dev.mslalith.focuslauncher.data.utils.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class LocationData(
    val latitude: Double,
    val longitude: Double,
)

interface LocationManager {
    suspend fun getCurrentLocation(): LocationData
}

internal class LocationManagerImpl(
    private val context: Context
) : LocationManager {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getCurrentLocation(): LocationData = suspendCancellableCoroutine { continuation ->
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            continuation.resume(LocationData(latitude = 0.0, longitude = 0.0))
            return@suspendCancellableCoroutine
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it == null) {
                continuation.resume(LocationData(latitude = 0.0, longitude = 0.0))
                return@addOnSuccessListener
            }
            val locationData = LocationData(
                latitude = it.latitude,
                longitude = it.longitude
            )
            continuation.resume(locationData)
        }.addOnFailureListener {
            continuation.resumeWithException(it)
        }
    }
}
