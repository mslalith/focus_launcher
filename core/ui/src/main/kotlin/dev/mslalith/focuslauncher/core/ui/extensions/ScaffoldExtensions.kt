package dev.mslalith.focuslauncher.core.ui.extensions

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult

suspend fun ScaffoldState.showSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    dismissVisibleSnackbar: Boolean = false,
    discardIfShowing: Boolean = false,
    actionLabel: String? = null,
    onAction: ((SnackbarResult) -> Unit)? = null
) {
    snackbarHostState.apply {
        if (discardIfShowing && currentSnackbarData != null) {
            return@apply
        }

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
}
