package dev.mslalith.focuslauncher.extensions

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun HorizontalSpacer(spacing: Dp) = Spacer(modifier = Modifier.width(width = spacing))

@Composable
fun VerticalSpacer(spacing: Dp) = Spacer(modifier = Modifier.height(height = spacing))

@Composable
fun RowScope.FillSpacer() = Spacer(modifier = Modifier.weight(weight = 1f))

@Composable
fun ColumnScope.FillSpacer() = Spacer(modifier = Modifier.weight(weight = 1f))

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
            duration = duration,
        )
        onAction?.invoke(snackbarResult)
    }
}
