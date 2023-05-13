package dev.mslalith.focuslauncher.core.ui.managers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import dev.mslalith.focuslauncher.core.ui.extensions.showDismissibleSnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class LauncherViewManager(
    private val coroutineScope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState,
    private val bottomSheetState: SheetState
) {

    private val _sheetContentTypeFlow = MutableStateFlow<(@Composable () -> Unit)?>(value = null)
    val sheetContentTypeStateFlow = _sheetContentTypeFlow.asStateFlow()

    val isBottomSheetVisible: Boolean
        get() = bottomSheetState.isVisible

    fun hideBottomSheet() {
        coroutineScope
            .launch { bottomSheetState.hide() }
            .invokeOnCompletion { _sheetContentTypeFlow.value = null }
    }

    fun showBottomSheet(content: @Composable () -> Unit) {
        _sheetContentTypeFlow.value = content
        coroutineScope.launch { bottomSheetState.show() }
    }

    /**
     * SnackBars
     */
    suspend fun showSnackbar(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,
        dismissVisibleSnackbar: Boolean = false,
        discardIfShowing: Boolean = false,
        actionLabel: String? = null,
        onAction: ((SnackbarResult) -> Unit)? = null
    ) = snackbarHostState.showDismissibleSnackbar(
        message = message,
        duration = duration,
        dismissVisibleSnackbar = dismissVisibleSnackbar,
        discardIfShowing = discardIfShowing,
        actionLabel = actionLabel,
        onAction = onAction
    )
}
