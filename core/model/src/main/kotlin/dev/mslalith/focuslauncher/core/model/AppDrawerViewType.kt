package dev.mslalith.focuslauncher.core.model

enum class AppDrawerViewType(
    val index: Int,
    val uiText: UiText
) {
    LIST(
        index = 0,
        uiText = UiText.Resource(stringRes = R.string.list)
    ),
    GRID(
        index = 1,
        uiText = UiText.Resource(stringRes = R.string.grid)
    )
}
