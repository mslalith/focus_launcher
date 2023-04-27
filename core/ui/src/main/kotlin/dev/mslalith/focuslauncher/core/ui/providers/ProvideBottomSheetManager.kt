package dev.mslalith.focuslauncher.core.ui.providers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.managers.LauncherViewManager

val LocalLauncherViewManager = staticCompositionLocalOf<LauncherViewManager> {
    error("No LauncherViewManager provided")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvideBottomSheetManager(
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val viewManager = remember {
        LauncherViewManager(
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            bottomSheetState = bottomSheetState
        )
    }
    CompositionLocalProvider(LocalLauncherViewManager provides viewManager) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBottomSheetContent() {
    val viewManager = LocalLauncherViewManager.current
    val content by viewManager.sheetContentTypeStateFlow.collectAsStateWithLifecycle()

    if (content != null) {
        ModalBottomSheet(
            onDismissRequest = viewManager::hideBottomSheet,
            sheetState = viewManager.bottomSheetState,
            content = { content?.invoke() }
        )
    }
}
