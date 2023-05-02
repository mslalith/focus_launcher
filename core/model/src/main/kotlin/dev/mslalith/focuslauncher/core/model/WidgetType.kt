package dev.mslalith.focuslauncher.core.model

enum class WidgetType(val uiText: UiText) {
    CLOCK(uiText = UiText.Resource(stringRes = R.string.clock)),
    LUNAR_PHASE(uiText = UiText.Resource(stringRes = R.string.lunar_phase)),
    QUOTES(uiText = UiText.Resource(stringRes = R.string.quotes))
}
