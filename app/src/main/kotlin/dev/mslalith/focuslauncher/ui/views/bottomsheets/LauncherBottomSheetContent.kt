package dev.mslalith.focuslauncher.ui.views.bottomsheets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager

@Composable
fun LauncherBottomSheetContent() {
    val viewManager = LocalLauncherViewManager.current
    val content by viewManager.sheetContentTypeStateFlow.collectAsState()

    Box(
        modifier = Modifier.navigationBarsPadding()
    ) {
        content?.invoke()
    }
}
