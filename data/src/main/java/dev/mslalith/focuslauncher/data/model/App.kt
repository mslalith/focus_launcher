package dev.mslalith.focuslauncher.data.model

data class App(
    val name: String,
    val displayName: String = name,
    val packageName: String,
    val isSystem: Boolean,
)

data class SelectedApp(
    val app: App,
    val isSelected: Boolean,
    val disabled: Boolean = false,
)
