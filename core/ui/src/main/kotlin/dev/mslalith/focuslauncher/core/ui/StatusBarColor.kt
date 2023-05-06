package dev.mslalith.focuslauncher.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.mslalith.focuslauncher.core.ui.providers.LocalSystemUiController

@Composable
fun StatusBarColor(
    hasTopAppBar: Boolean
) {
    val systemUiController = LocalSystemUiController.current
    val color = MaterialTheme.colorScheme.surface

    LaunchedEffect(key1 = systemUiController, key2 = color) {
        systemUiController.setStatusBarColor(color = color)
    }
}
