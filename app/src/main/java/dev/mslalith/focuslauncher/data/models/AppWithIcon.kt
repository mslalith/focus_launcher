package dev.mslalith.focuslauncher.data.models

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.data.database.entities.App

data class SelectedApp(
    val app: App,
    val isSelected: Boolean,
    val disabled: Boolean = false,
)

data class AppWithIcon(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    val isSystem: Boolean
) {
    fun toApp() = App(
        name = name,
        packageName = packageName,
        isSystem = isSystem
    )
}
