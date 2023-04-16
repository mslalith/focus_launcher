package dev.mslalith.focuslauncher.screens.iconpack.model

import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon

internal data class IconPackState(
    val allApps: List<AppWithIcon>,
    val iconPacks: List<AppWithIcon>,
    val iconPackType: IconPackType?
)
