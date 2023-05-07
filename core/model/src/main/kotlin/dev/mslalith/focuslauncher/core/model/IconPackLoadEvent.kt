package dev.mslalith.focuslauncher.core.model

sealed interface IconPackLoadEvent {
    object None : IconPackLoadEvent
    object Loading : IconPackLoadEvent
    object Loaded : IconPackLoadEvent
    object Reloading : IconPackLoadEvent
    object Reloaded : IconPackLoadEvent
}
