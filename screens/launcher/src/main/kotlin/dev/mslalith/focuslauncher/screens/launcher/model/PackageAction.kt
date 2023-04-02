package dev.mslalith.focuslauncher.screens.launcher.model

internal sealed interface PackageAction {
    data class Added(val packageName: String) : PackageAction
    data class Updated(val packageName: String) : PackageAction
    data class Removed(val packageName: String) : PackageAction
}
