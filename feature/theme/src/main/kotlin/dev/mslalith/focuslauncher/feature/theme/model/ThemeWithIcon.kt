package dev.mslalith.focuslauncher.feature.theme.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import dev.mslalith.focuslauncher.core.model.Theme

@Immutable
internal data class ThemeWithIcon(
    val theme: Theme,
    @DrawableRes val iconRes: Int
)
