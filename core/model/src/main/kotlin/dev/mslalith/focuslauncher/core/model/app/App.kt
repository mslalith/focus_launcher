package dev.mslalith.focuslauncher.core.model.app

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class App(
    val name: String,
    val displayName: String = name,
    val packageName: String,
    val isSystem: Boolean
)
