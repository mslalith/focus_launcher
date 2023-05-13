package dev.mslalith.focuslauncher.feature.theme

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.feature.theme.model.ThemeWithIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ThemeSelectionSheet(
    modifier: Modifier = Modifier,
    closeBottomSheet: () -> Unit
) {
    ThemeSelectionSheetInternal(
        modifier = modifier,
        closeBottomSheet = closeBottomSheet
    )
}

@Composable
internal fun ThemeSelectionSheetInternal(
    modifier: Modifier = Modifier,
    launcherThemeViewModel: LauncherThemeViewModel = hiltViewModel(),
    closeBottomSheet: () -> Unit
) {
    val allThemes = remember {
        Theme.values().map { theme ->
            ThemeWithIcon(
                theme = theme,
                iconRes = when (theme) {
                    Theme.FOLLOW_SYSTEM -> R.drawable.ic_device_mobile
                    Theme.NOT_WHITE -> R.drawable.ic_sun_dim
                    Theme.SAID_DARK -> R.drawable.ic_moon_stars
                }
            )
        }.toImmutableList()
    }

    ThemeSelectionSheetInternal(
        modifier = modifier,
        currentTheme = launcherThemeViewModel.currentTheme.collectAsStateWithLifecycle().value,
        allThemes = allThemes,
        onThemeSelected = { theme ->
            if (theme != null) launcherThemeViewModel.changeTheme(theme)
            closeBottomSheet()
        }
    )
}

@Composable
internal fun ThemeSelectionSheetInternal(
    modifier: Modifier = Modifier,
    currentTheme: Theme,
    allThemes: ImmutableList<ThemeWithIcon>,
    onThemeSelected: (Theme?) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = allThemes,
            key = { it.theme }
        ) { themeWithIcon ->
            ThemeSelectionListItem(
                theme = themeWithIcon.theme,
                iconRes = themeWithIcon.iconRes,
                isSelected = themeWithIcon.theme == currentTheme,
                onClick = { onThemeSelected(if (currentTheme != themeWithIcon.theme) themeWithIcon.theme else null) }
            )
        }
    }
}
