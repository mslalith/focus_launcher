package dev.mslalith.focuslauncher.core.model

sealed class Screen(val id: String) {
    object Launcher : Screen(id = "screen_launcher")
    object EditFavorites : Screen(id = "screen_edit_favorites")
    object HideApps : Screen(id = "screen_hide_apps")
    object CurrentPlace : Screen(id = "screen_current_place")
    object IconPack : Screen(id = "screen_icon_packs")
    object About : Screen(id = "screen_about")
}
