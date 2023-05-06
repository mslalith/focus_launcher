package dev.mslalith.focuslauncher.core.ui.providers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
