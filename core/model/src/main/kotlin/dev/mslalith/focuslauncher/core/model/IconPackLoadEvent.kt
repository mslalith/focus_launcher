package dev.mslalith.focuslauncher.core.model

sealed class IconPackLoadEvent(open val iconPackType: IconPackType) {
    data class Loading(override val iconPackType: IconPackType) : IconPackLoadEvent(iconPackType = iconPackType)
    data class Loaded(override val iconPackType: IconPackType) : IconPackLoadEvent(iconPackType = iconPackType)
    data class Reloading(override val iconPackType: IconPackType) : IconPackLoadEvent(iconPackType = iconPackType)
    data class Reloaded(override val iconPackType: IconPackType) : IconPackLoadEvent(iconPackType = iconPackType)
}

val IconPackLoadEvent.isTerminal: Boolean
    get() = when (this) {
        is IconPackLoadEvent.Loaded, is IconPackLoadEvent.Reloaded -> true
        else -> false
    }
