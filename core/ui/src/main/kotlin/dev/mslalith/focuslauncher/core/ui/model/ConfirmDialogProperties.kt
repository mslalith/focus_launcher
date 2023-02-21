package dev.mslalith.focuslauncher.core.ui.model

data class ConfirmDialogProperties(
    val title: String,
    val message: String,
    val confirmButtonText: String = "Confirm",
    val cancelButtonText: String = "Cancel",
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit
)
