package dev.mslalith.focuslauncher.core.model.appdrawer

import dev.mslalith.focuslauncher.core.model.R
import dev.mslalith.focuslauncher.core.model.UiText

enum class AppDrawerIconViewType(
    val index: Int,
    val uiText: UiText
) {
    TEXT(index = 1, uiText = UiText.Resource(stringRes = R.string.text)),
    ICONS(index = 2, uiText = UiText.Resource(stringRes = R.string.icons)),
    COLORED(index = 3, uiText = UiText.Resource(stringRes = R.string.colored))
}
