package dev.mslalith.focuslauncher.core.model

enum class ClockAlignment(
    val index: Int,
    val uiText: UiText
) {
    START(
        index = 0,
        uiText = UiText.Resource(stringRes = R.string.start)
    ),
    CENTER(
        index = 1,
        uiText = UiText.Resource(stringRes = R.string.center)
    ),
    END(
        index = 2,
        uiText = UiText.Resource(stringRes = R.string.end)
    )
}
