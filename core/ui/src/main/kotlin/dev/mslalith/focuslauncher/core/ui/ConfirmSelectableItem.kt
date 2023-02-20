package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.ui.model.ConfirmSelectableItemType

@Composable
fun ConfirmSelectableItem(
    text: String,
    confirmMessage: String,
    itemType: ConfirmSelectableItemType,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    height: Dp = 48.dp,
    iconWidth: Dp = 56.dp,
    onConfirm: (Boolean) -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }

    Column {
        when (itemType) {
            is ConfirmSelectableItemType.Icon -> SelectableIconItem(
                text = text,
                iconRes = itemType.iconRes,
                contentDescription = itemType.contentDescription,
                height = height,
                iconWidth = iconWidth,
                onClick = { showConfirm = !showConfirm }
            )

            is ConfirmSelectableItemType.Checkbox -> SelectableCheckboxItem(
                text = text,
                checked = itemType.checked,
                disabled = itemType.disabled,
                height = height,
                iconWidth = iconWidth,
                onClick = { showConfirm = !showConfirm }
            )
        }
        AnimatedVisibility(visible = showConfirm) {
            Column(
                modifier = Modifier.padding(horizontal = 22.dp)
            ) {
                Text(
                    text = confirmMessage,
                    style = TextStyle(
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    TextButton(
                        text = cancelText,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showConfirm = false
                            onConfirm(false)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        text = confirmText,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showConfirm = false
                            onConfirm(true)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
