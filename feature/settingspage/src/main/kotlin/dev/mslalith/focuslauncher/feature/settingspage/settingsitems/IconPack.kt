package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags

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
        )
    ) {
        SettingsItem(
            modifier = Modifier.testSemantics(tag = TestTags.ITEM_ICON_PACK),
            text = stringResource(id = R.string.icon_pack),
            onClick = onClick
        )
    }
}
