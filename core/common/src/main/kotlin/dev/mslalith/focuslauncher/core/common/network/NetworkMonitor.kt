package dev.mslalith.focuslauncher.core.common.network

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
    fun isCurrentlyConnected(): Boolean
}
