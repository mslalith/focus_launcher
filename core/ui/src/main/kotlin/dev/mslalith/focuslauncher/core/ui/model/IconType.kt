package dev.mslalith.focuslauncher.core.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed class IconType {
    data class Vector(val imageVector: ImageVector) : IconType()
    data class Resource(@DrawableRes val resId: Int) : IconType()
}
