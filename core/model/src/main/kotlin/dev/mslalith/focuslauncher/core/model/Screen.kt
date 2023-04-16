package dev.mslalith.focuslauncher.core.model

sealed class Screen(val id: String) {
    object Launcher : Screen("screen_launcher")
    object EditFavorites : Screen("screen_edit_favorites")
    object HideApps : Screen("screen_hide_apps")
    object CurrentPlace : Screen("screen_current_place")
    object IconPack : Screen("screen_icon_packs")
}
