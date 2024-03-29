package dev.mslalith.focuslauncher.core.common.network.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.common.network.NetworkMonitor
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

@IgnoreInKoverReport
internal class ConnectivityManagerNetworkMonitorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkMonitor {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    override val isOnline: Flow<Boolean> = callbackFlow {

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                channel.trySend(element = isCurrentlyConnected())
            }

            override fun onLost(network: Network) {
                channel.trySend(element = isCurrentlyConnected())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                channel.trySend(element = isCurrentlyConnected())
            }
        }

        connectivityManager?.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            callback
        )

        channel.trySend(element = isCurrentlyConnected())

        awaitClose {
            connectivityManager?.unregisterNetworkCallback(callback)
        }
    }.conflate()

    override fun isCurrentlyConnected() = connectivityManager?.activeNetwork
        ?.let(connectivityManager::getNetworkCapabilities)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ?: false
}
