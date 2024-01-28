package dev.mslalith.focuslauncher.core.ui.providers

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import dev.mslalith.focuslauncher.core.ui.controller.SystemUiController
import dev.mslalith.focuslauncher.core.ui.controller.SystemUiControllerImpl

@IgnoreInKoverReport
val LocalSystemUiController = compositionLocalOf<SystemUiController> {
    error("No SystemUiController provided")
}

@Composable
fun ProvideSystemUiController(
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    CompositionLocalProvider(LocalSystemUiController provides systemUiController) {
        content()
    }
}

@Composable
private fun rememberSystemUiController(): SystemUiController {
    val view = LocalView.current
    val window = (view.context as Activity).window

    return remember(window, view) {
        SystemUiControllerImpl(window, view)
    }
}
