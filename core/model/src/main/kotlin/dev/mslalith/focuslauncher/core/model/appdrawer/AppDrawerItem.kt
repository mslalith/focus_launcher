package dev.mslalith.focuslauncher.core.model.appdrawer

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.model.app.App

@Immutable
data class AppDrawerItem(
    val app: App,
    val isFavorite: Boolean,
    val icon: Drawable,
    val color: Color?
) {
    val uniqueKey: Int
        get() = app.packageName.hashCode() + (31 * icon.hashCode())
}
