package dev.mslalith.focuslauncher.core.ui.model

import androidx.annotation.DrawableRes

sealed class ConfirmSelectableItemType {
    data class Icon(
        @DrawableRes val iconRes: Int,
        val contentDescription: String? = null
    ) : ConfirmSelectableItemType()

    data class Checkbox(
        val checked: Boolean,
        val disabled: Boolean = false
    ) : ConfirmSelectableItemType()
}
