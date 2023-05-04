package dev.mslalith.focuslauncher.feature.theme

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.model.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ThemeSelectionSheet(
    closeBottomSheet: () -> Unit
) {
    ThemeSelectionSheetInternal(
        closeBottomSheet = closeBottomSheet
    )
}

@Composable
internal fun ThemeSelectionSheetInternal(
    launcherThemeViewModel: LauncherThemeViewModel = hiltViewModel(),
    closeBottomSheet: () -> Unit
) {
    val allThemes = remember {
        Theme.values().toList().toImmutableList()
    }

    ThemeSelectionSheetInternal(
        currentTheme = launcherThemeViewModel.currentTheme.collectAsStateWithLifecycle().value,
        allThemes = allThemes,
        onThemeSelected = {
            launcherThemeViewModel.changeTheme(it)
            closeBottomSheet()
        }
    )
}

@Composable
internal fun ThemeSelectionSheetInternal(
    currentTheme: Theme,
    allThemes: ImmutableList<Theme>,
    onThemeSelected: (Theme) -> Unit
) {
    LazyColumn {
        items(
            items = allThemes,
            key = { it }
        ) { theme ->
            ThemeSelectionListItem(
                theme = theme,
                isSelected = theme == currentTheme,
                onClick = { onThemeSelected(theme) }
            )
        }
    }
}
