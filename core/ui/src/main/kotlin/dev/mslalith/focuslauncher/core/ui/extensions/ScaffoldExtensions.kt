package dev.mslalith.focuslauncher.core.ui.extensions

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

internal suspend fun SnackbarHostState.showDismissibleSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    dismissVisibleSnackbar: Boolean = false,
    discardIfShowing: Boolean = false,
    actionLabel: String? = null,
    onAction: ((SnackbarResult) -> Unit)? = null
) {
    if (discardIfShowing && currentSnackbarData != null) return

    if (dismissVisibleSnackbar) {
        currentSnackbarData?.dismiss()
    }

    val snackbarResult = showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration
    )
    onAction?.invoke(snackbarResult)
}
