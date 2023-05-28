package dev.mslalith.focuslauncher.core.model.app

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.model.extensions.generateHashCode

@Immutable
data class AppWithIcon(
    val app: App,
    val icon: Drawable
) {
    val uniqueKey: Int
        get() = listOf(app.packageName, icon).generateHashCode()
}
