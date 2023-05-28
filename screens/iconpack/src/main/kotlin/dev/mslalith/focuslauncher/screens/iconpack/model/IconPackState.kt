package dev.mslalith.focuslauncher.screens.iconpack.model

import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.common.model.LoadingState
import dev.mslalith.focuslauncher.core.model.IconPackType
import dev.mslalith.focuslauncher.core.model.app.AppWithIcon
import dev.mslalith.focuslauncher.core.model.appdrawer.AppDrawerItem
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class IconPackState(
    val allApps: LoadingState<ImmutableList<AppDrawerItem>>,
    val iconPacks: ImmutableList<AppWithIcon>,
    val iconPackType: IconPackType?,
    val canSave: Boolean
)
