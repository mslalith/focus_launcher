package dev.mslalith.focuslauncher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_THEME
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.ui.providers.LocalSystemUiController
import dev.mslalith.focuslauncher.feature.settingspage.ThemeViewModel
import kotlinx.coroutines.flow.first

private val NotWhitePalette = lightColors(
    background = notWhiteBackgroundColor,
    surface = notWhiteBackgroundColor,
    onBackground = notWhiteOnBackgroundColor,
    onSurface = notWhiteOnBackgroundColor,
    primary = notWhitePrimaryColorVariant,
    primaryVariant = notWhitePrimaryColorVariant,
    secondary = notWhiteSecondaryColorVariant,
    secondaryVariant = notWhiteSecondaryColorVariant
)

private val SaidDarkPalette = darkColors(
    background = saidDarkBackgroundColor,
    surface = saidDarkBackgroundColor,
    onBackground = saidDarkOnBackgroundColor,
    onSurface = saidDarkOnBackgroundColor,
    primary = saidDarkPrimaryColorVariant,
    primaryVariant = saidDarkPrimaryColorVariant,
    secondary = saidDarkSecondaryColorVariant,
    secondaryVariant = saidDarkSecondaryColorVariant
)

@Composable
fun FocusLauncherTheme(
    content: @Composable () -> Unit
) {
    val systemUiController = LocalSystemUiController.current
    val themeViewModel: ThemeViewModel = viewModel()

    val currentTheme by themeViewModel.currentThemeStateFlow.collectAsStateWithLifecycle()
    val theme = currentTheme ?: if (isSystemInDarkTheme()) Theme.SAID_DARK else DEFAULT_THEME

    val colors = when (theme) {
        Theme.NOT_WHITE -> NotWhitePalette
        Theme.SAID_DARK -> SaidDarkPalette
    }
    systemUiController.setSystemBarsColor(color = colors.background)

    LaunchedEffect(Unit) {
        if (themeViewModel.firstRunStateFlow.first()) {
            themeViewModel.changeTheme(theme)
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
