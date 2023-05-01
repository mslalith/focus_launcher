package dev.mslalith.focuslauncher.screens.iconpack.model

import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.model.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType

internal data class IconPackState(
    val allApps: LoadingState<List<AppWithIcon>>,
    val iconPacks: List<AppWithIcon>,
    val iconPackType: IconPackType?,
    val canSave: Boolean
)
