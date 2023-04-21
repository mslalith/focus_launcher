package dev.mslalith.focuslauncher.ui.views

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.ui.views.bottomsheets.LauncherBottomSheetContent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent(
    content: @Composable () -> Unit
) {
    val viewManager = LocalLauncherViewManager.current

    ModalBottomSheetLayout(
        sheetState = viewManager.bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        sheetShape = MaterialTheme.shapes.extraLarge,
        scrimColor = Color.Black.copy(alpha = 0.7f),
        sheetContent = { LauncherBottomSheetContent() }
    ) {
        content()
    }
}
