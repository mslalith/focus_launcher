package dev.mslalith.focuslauncher.screens.iconpack.model

import dev.mslalith.focuslauncher.core.common.LoadingState
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithIconFavorite

internal data class IconPackState(
    val allApps: LoadingState<List<AppWithIconFavorite>>,
    val iconPacks: List<AppWithIcon>,
    val iconPackType: IconPackType?,
    val canSave: Boolean
)
