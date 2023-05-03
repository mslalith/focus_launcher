package dev.mslalith.focuslauncher.core.model.app

import androidx.compose.runtime.Immutable

@Immutable
data class App(
    val name: String,
    val displayName: String = name,
    val packageName: String,
    val isSystem: Boolean
)
