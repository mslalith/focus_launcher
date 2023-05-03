package dev.mslalith.focuslauncher.feature.favorites.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.FillSpacer
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer
import dev.mslalith.focuslauncher.feature.favorites.R
import dev.mslalith.focuslauncher.feature.favorites.model.FavoritesContextMode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.reflect.KClass

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun FavoritesContextHeader(
    currentContextMode: FavoritesContextMode,
    changeContextModeToOpen: () -> Unit,
    onReorderClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    fun handleReClickFor(contextMode: FavoritesContextMode, action: () -> Unit) {
        if (currentContextMode == contextMode) changeContextModeToOpen() else action()
    }

    @Composable
    fun headerText(): String = when (currentContextMode) {
        FavoritesContextMode.Open, FavoritesContextMode.Closed -> stringResource(id = R.string.favorites)
        FavoritesContextMode.Remove -> stringResource(id = R.string.remove)
        FavoritesContextMode.Reorder, is FavoritesContextMode.ReorderPickPosition -> stringResource(id = R.string.reorder)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedContent(
            label = "Animated Header",
            targetState = headerText(),
            transitionSpec = {
                slideInVertically { it } + fadeIn() with slideOutVertically { -it } + fadeOut() using SizeTransform(clip = false)
            }
        ) { header ->
            Text(
                text = header,
                style = MaterialTheme.typography.titleLarge
            )
        }
        FillSpacer()
        FavoritesContextActionItem(
            contextModes = persistentListOf(FavoritesContextMode.Reorder::class, FavoritesContextMode.ReorderPickPosition::class) as ImmutableList<KClass<FavoritesContextMode>>,
            currentContextMode = currentContextMode,
            iconRes = R.drawable.ic_drag_indicator,
            onClick = {
                handleReClickFor(contextMode = FavoritesContextMode.Reorder) { onReorderClick() }
            }
        )
        HorizontalSpacer(spacing = 4.dp)
        FavoritesContextActionItem(
            contextModes = persistentListOf(FavoritesContextMode.Remove::class) as ImmutableList<KClass<FavoritesContextMode>>,
            currentContextMode = currentContextMode,
            iconRes = R.drawable.ic_delete,
            onClick = {
                handleReClickFor(contextMode = FavoritesContextMode.Remove) { onRemoveClick() }
            }
        )
    }
}
