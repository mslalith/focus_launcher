package dev.mslalith.focuslauncher.core.ui.extensions

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport

@IgnoreInKoverReport
suspend fun SnackbarHostState.showDismissibleSnackbar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    dismissVisibleSnackbar: Boolean = true,
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
