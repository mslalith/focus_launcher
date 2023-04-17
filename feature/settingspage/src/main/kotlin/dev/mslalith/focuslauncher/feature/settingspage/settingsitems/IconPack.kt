package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun IconPack(
    shouldShow: Boolean,
    onClick: () -> Unit
) {
    val durationMillis = 350
    AnimatedVisibility(
        visible = shouldShow,
        enter = fadeIn(
            animationSpec = tween(durationMillis = durationMillis)
        ) + expandVertically(
            animationSpec = tween(durationMillis = durationMillis)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = durationMillis)
        ) + shrinkVertically(
            animationSpec = tween(durationMillis = durationMillis)
        ),
    ) {
        SettingsItem(
            text = "Icon Pack",
            onClick = onClick
        )
    }
}
