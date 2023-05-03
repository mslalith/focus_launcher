package dev.mslalith.focuslauncher.core.model.app

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable

@Immutable
data class AppWithIcon(
    val name: String,
    val displayName: String,
    val packageName: String,
    val icon: Drawable,
    val isSystem: Boolean
) {
    val uniqueKey: Int
        get() = packageName.hashCode() + (31 * icon.hashCode())

    fun toApp() = App(
        name = name,
        displayName = displayName,
        packageName = packageName,
        isSystem = isSystem
    )
}
