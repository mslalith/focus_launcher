package dev.mslalith.focuslauncher.core.model.appdrawer

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.model.app.App
import dev.mslalith.focuslauncher.core.model.extensions.generateHashCode

@Immutable
data class AppDrawerItem(
    val app: App,
    val isFavorite: Boolean,
    val icon: Drawable,
    val color: Color?
) {
    val uniqueKey: Int
        get() = listOf(app.packageName, icon, color).generateHashCode()
}
