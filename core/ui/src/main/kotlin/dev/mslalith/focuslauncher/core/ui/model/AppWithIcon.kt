package dev.mslalith.focuslauncher.core.ui.model

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.core.model.App

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
