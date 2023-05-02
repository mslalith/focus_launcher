package dev.mslalith.focuslauncher.core.model.app

data class App(
    val name: String,
    val displayName: String = name,
    val packageName: String,
    val isSystem: Boolean
)
