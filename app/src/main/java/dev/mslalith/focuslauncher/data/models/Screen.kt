package dev.mslalith.focuslauncher.data.models

sealed class Screen(val id: String) {
    object Launcher : Screen("screen_launcher")
    object EditFavorites : Screen("screen_edit_favorites")
    object HideApps : Screen("screen_hide_apps")
    object PickPlaceForLunarPhase: Screen("screen_pick_place_for_lunar_phase")
}
