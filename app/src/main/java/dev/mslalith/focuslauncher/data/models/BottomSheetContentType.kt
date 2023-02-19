package dev.mslalith.focuslauncher.data.models

sealed class BottomSheetContentType {
    data class AppDrawer(val properties: AppDrawerSettingsProperties) : BottomSheetContentType()
    data class MoreAppOptions(val properties: MoreAppOptionsProperties) : BottomSheetContentType()

    sealed class Widgets : BottomSheetContentType() {
        data class Clock(val properties: ClockSettingsProperties) : Widgets()
        data class LunarPhase(val properties: LunarPhaseSettingsProperties) : Widgets()
        object Quotes : Widgets()
    }
}

enum class WidgetType(val text: String) {
    CLOCK(text = "Clock"),
    LUNAR_PHASE(text = "Lunar Phase"),
    QUOTES(text = "Quotes")
}
