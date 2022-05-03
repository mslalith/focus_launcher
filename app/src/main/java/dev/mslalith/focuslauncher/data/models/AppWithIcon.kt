package dev.mslalith.focuslauncher.data.models

import android.graphics.drawable.Drawable
import dev.mslalith.focuslauncher.data.database.entities.AppRoom

data class SelectedApp(
    val app: AppRoom,
    val isSelected: Boolean,
    val disabled: Boolean = false,
)

data class AppWithIcon(
    val name: String,
    val packageName: String,
    val icon: Drawable,
    val isSystem: Boolean
) {
    fun toApp() = AppRoom(
        name = name,
        packageName = packageName,
        isSystem = isSystem
    )
}
