package dev.mslalith.focuslauncher.core.model

var BUILD_FLAVOR: BuildFlavor = BuildFlavor.Store

enum class BuildFlavor(val id: String) {
    Dev(id = "dev"),
    Store(id = "store");

    companion object {
        fun fromId(id: String) = entries.firstOrNull { it.id == id } ?: Store
    }

    fun isDev(): Boolean = this == Dev
}
