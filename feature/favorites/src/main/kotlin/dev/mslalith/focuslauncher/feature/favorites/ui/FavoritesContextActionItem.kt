package dev.mslalith.focuslauncher.feature.favorites.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode
import kotlinx.collections.immutable.ImmutableList
import kotlin.reflect.KClass

@Composable
internal fun FavoritesContextActionItem(
    contextModes: ImmutableList<KClass<out FavoritesContextMode>>,
    currentContextMode: FavoritesContextMode,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    val iconBackgroundColor by animateColorAsState(
        label = "Icon Background color",
        targetValue = if (currentContextMode::class in contextModes) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    val iconColor by animateColorAsState(
        label = "Icon color",
        targetValue = if (currentContextMode::class in contextModes) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    RoundIcon(
        iconRes = iconRes,
        backgroundColor = iconBackgroundColor,
        contentColor = iconColor,
        onClick = onClick
    )
}
