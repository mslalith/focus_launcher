package dev.mslalith.focuslauncher.core.model

import androidx.compose.runtime.Immutable

@Immutable
data class Quote(
    val id: String,
    val quote: String,
    val author: String
)
