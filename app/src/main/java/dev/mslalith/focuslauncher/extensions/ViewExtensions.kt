package dev.mslalith.focuslauncher.extensions

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@SuppressLint("ComposableNaming")
@Composable
fun Dp.horizontalSpacer() = Spacer(modifier = Modifier.width(width = this))

@SuppressLint("ComposableNaming")
@Composable
fun Dp.verticalSpacer() = Spacer(modifier = Modifier.height(height = this))

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
