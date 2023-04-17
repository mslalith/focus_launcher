package dev.mslalith.focuslauncher.core.model

sealed interface IconPackType {
    object System : IconPackType
    data class Custom(val packageName: String) : IconPackType

    val value: String
        get() = when (this) {
            is Custom -> packageName
            System -> "default"
        }

    companion object {
        fun isSystemPack(value: String): Boolean = value == "default"

        fun from(value: String): IconPackType = when (value) {
            "default" -> System
            else -> Custom(packageName = value)
        }
    }
}
