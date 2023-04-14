package dev.mslalith.focuslauncher.ui.views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.focuslauncher.core.ui.ConfirmDialog
import dev.mslalith.focuslauncher.core.ui.providers.LocalLauncherViewManager
import dev.mslalith.focuslauncher.ui.views.bottomsheets.LauncherBottomSheetContent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent(
    content: @Composable () -> Unit
) {
    val viewManager = LocalLauncherViewManager.current
    val dialogProperties by viewManager.dialogPropertiesStateFlow.collectAsStateWithLifecycle()

    ModalBottomSheetLayout(
        sheetState = viewManager.bottomSheetState,
        sheetBackgroundColor = MaterialTheme.colors.primaryVariant,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        scrimColor = Color.Black.copy(alpha = 0.7f),
        sheetContent = { LauncherBottomSheetContent() }
    ) {
        content()
    }

    dialogProperties?.let { properties ->
        ConfirmDialog(properties = properties)
    }
}
