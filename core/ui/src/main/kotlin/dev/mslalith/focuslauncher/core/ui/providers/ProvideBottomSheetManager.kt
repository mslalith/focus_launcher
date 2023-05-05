package dev.mslalith.focuslauncher.core.ui.providers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.managers.LauncherViewManager

val LocalLauncherViewManager = compositionLocalOf<LauncherViewManager> {
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
        AppBottomSheetContent(sheetState = bottomSheetState)
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBottomSheetContent(
    sheetState: SheetState
) {
    val viewManager = LocalLauncherViewManager.current
    val content by viewManager.sheetContentTypeStateFlow.collectAsStateWithLifecycle()

    if (content != null) {
        ModalBottomSheet(
            onDismissRequest = viewManager::hideBottomSheet,
            sheetState = sheetState,
            content = { content?.invoke() }
        )
    }
}
