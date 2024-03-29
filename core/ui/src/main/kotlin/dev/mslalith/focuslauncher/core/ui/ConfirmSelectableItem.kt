package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.ConfirmSelectableItemType
import dev.mslalith.focuslauncher.core.model.UiText
import dev.mslalith.focuslauncher.core.ui.extensions.string

@Composable
fun ConfirmSelectableItem(
    modifier: Modifier = Modifier,
    text: String,
    confirmMessage: String,
    itemType: ConfirmSelectableItemType,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    confirmUiText: UiText = UiText.Resource(stringRes = R.string.confirm),
    cancelText: UiText = UiText.Resource(stringRes = R.string.cancel),
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(value = false) }

    Column(
        modifier = modifier
    ) {
        when (itemType) {
            is ConfirmSelectableItemType.Icon -> SelectableIconItem(
                text = text,
                iconRes = itemType.iconRes,
                contentDescription = itemType.contentDescription,
                contentColor = contentColor,
                onClick = { showConfirm = !showConfirm }
            )

            is ConfirmSelectableItemType.Checkbox -> SelectableCheckboxItem(
                text = text,
                checked = itemType.checked,
                disabled = itemType.disabled,
                onClick = { showConfirm = !showConfirm }
            )
        }
        AnimatedVisibility(visible = showConfirm) {
            Column(
                modifier = Modifier
                    .background(color = backgroundColor)
                    .padding(horizontal = 22.dp)
            ) {
                Text(
                    text = confirmMessage,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium
                )
                VerticalSpacer(spacing = 12.dp)
                Row {
                    TextButton(
                        text = cancelText.string(),
                        modifier = Modifier.weight(weight = 1f),
                        backgroundColor = backgroundColor,
                        textColor = contentColor,
                        onClick = {
                            showConfirm = false
                            onCancel()
                        }
                    )
                    HorizontalSpacer(spacing = 8.dp)
                    TextButton(
                        text = confirmUiText.string(),
                        modifier = Modifier.weight(1f),
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = {
                            showConfirm = false
                            onConfirm()
                        }
                    )
                }
                VerticalSpacer(spacing = 8.dp)
            }
        }
    }
}
