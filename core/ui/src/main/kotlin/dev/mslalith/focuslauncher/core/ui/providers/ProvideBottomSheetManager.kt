package dev.mslalith.focuslauncher.core.ui.providers

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import dev.mslalith.focuslauncher.core.ui.managers.LauncherViewManager

val LocalLauncherViewManager = staticCompositionLocalOf<LauncherViewManager> {
    error("No LauncherViewManager provided")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProvideBottomSheetManager(
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val viewManager = remember {
        LauncherViewManager(
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            bottomSheetState = modalBottomSheetState
        )
    }
    CompositionLocalProvider(LocalLauncherViewManager provides viewManager) {
        content()
    }
}
