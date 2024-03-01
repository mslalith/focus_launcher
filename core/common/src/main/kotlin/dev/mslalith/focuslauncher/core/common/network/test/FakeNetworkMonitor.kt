package dev.mslalith.focuslauncher.core.common.network.test

import dev.mslalith.focuslauncher.core.common.network.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeNetworkMonitor : NetworkMonitor {

    private val _isOnline = MutableStateFlow(value = true)
    override val isOnline: Flow<Boolean> = _isOnline

    override fun isCurrentlyConnected(): Boolean = _isOnline.value

    fun goOnline() {
        _isOnline.value = true
    }

    fun goOffline() {
        _isOnline.value = false
    }
}
