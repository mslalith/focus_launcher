package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import dev.mslalith.focuslauncher.core.ui.providers.LocalSystemUiController

@Composable
fun StatusBarColor(
    hasTopAppBar: Boolean
) {
    val systemUiController = LocalSystemUiController.current
    val statusBarColor by animateColorAsState(
        label = "Status Bar color",
        targetValue = if (hasTopAppBar) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background
    )

    LaunchedEffect(key1 = statusBarColor) {
        systemUiController.setStatusBarColor(color = statusBarColor)
    }
}
