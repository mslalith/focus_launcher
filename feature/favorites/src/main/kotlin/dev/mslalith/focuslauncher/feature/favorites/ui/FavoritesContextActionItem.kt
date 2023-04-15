package dev.mslalith.focuslauncher.feature.favorites.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.RoundIcon
import dev.mslalith.focuslauncher.core.ui.model.IconType
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode
import kotlin.reflect.KClass

@Composable
internal fun FavoritesContextActionItem(
    contextModes: List<KClass<FavoritesContextMode>>,
    currentContextMode: FavoritesContextMode,
    iconType: IconType,
    onClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colors.background
    val onBackgroundColor = MaterialTheme.colors.onBackground
    val iconBackgroundColor by animateColorAsState(
        label = "Icon Background color",
        targetValue = if (currentContextMode::class in contextModes) onBackgroundColor else backgroundColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    val iconColor by animateColorAsState(
        label = "Icon color",
        targetValue = if (currentContextMode::class in contextModes) backgroundColor else onBackgroundColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    RoundIcon(
        iconSize = 40.dp,
        iconType = iconType,
        backgroundColor = iconBackgroundColor,
        iconColor = iconColor,
        onClick = onClick
    )
}
