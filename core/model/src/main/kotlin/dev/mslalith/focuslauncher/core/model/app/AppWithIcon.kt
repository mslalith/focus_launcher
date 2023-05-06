package dev.mslalith.focuslauncher.core.model.app

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable

@Immutable
data class AppWithIcon(
    val app: App,
    val icon: Drawable
) {
    val uniqueKey: Int
        get() = app.packageName.hashCode() + (31 * icon.hashCode())
}
