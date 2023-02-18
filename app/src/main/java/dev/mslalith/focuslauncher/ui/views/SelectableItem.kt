package dev.mslalith.focuslauncher.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SELECTABLE_ITEM_HEIGHT = 48.dp
private val SELECTABLE_ITEM_ICON_WIDTH = 56.dp

sealed class ConfirmSelectableItemType {
    data class Icon(
        @DrawableRes val iconRes: Int,
        val contentDescription: String? = null
    ) : ConfirmSelectableItemType()

    data class Checkbox(
        val checked: Boolean,
        val disabled: Boolean = false
    ) : ConfirmSelectableItemType()
}

@Composable
fun ConfirmSelectableItem(
    text: String,
    confirmMessage: String,
    itemType: ConfirmSelectableItemType,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    height: Dp = SELECTABLE_ITEM_HEIGHT,
    iconWidth: Dp = SELECTABLE_ITEM_ICON_WIDTH,
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

@Composable
fun SelectableCheckboxItem(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    disabled: Boolean = false,
    height: Dp = SELECTABLE_ITEM_HEIGHT,
    iconWidth: Dp = SELECTABLE_ITEM_ICON_WIDTH,
    onClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colors.background
    val onBackgroundColor = MaterialTheme.colors.onBackground
    val disabledColor = onBackgroundColor.copy(alpha = 0.4f)

    SelectableItem(
        modifier = modifier,
        text = text,
        height = height,
        iconWidth = iconWidth,
        onClick = onClick,
        leading = {
            Checkbox(
                checked = checked,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = if (disabled) disabledColor else onBackgroundColor,
                    uncheckedColor = if (disabled) disabledColor else onBackgroundColor,
                    checkmarkColor = backgroundColor
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .padding(start = 8.dp, end = 8.dp)
            )
        }
    )
}

@Composable
fun SelectableIconItem(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int,
    contentDescription: String? = null,
    height: Dp = SELECTABLE_ITEM_HEIGHT,
    iconWidth: Dp = SELECTABLE_ITEM_ICON_WIDTH,
    onClick: () -> Unit
) {
    SelectableItem(
        modifier = modifier,
        text = text,
        height = height,
        iconWidth = iconWidth,
        onClick = onClick,
        leading = {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription ?: text,
                tint = MaterialTheme.colors.onBackground
            )
        }
    )
}

@Composable
private fun SelectableItem(
    modifier: Modifier = Modifier,
    text: String,
    leading: @Composable () -> Unit,
    height: Dp = SELECTABLE_ITEM_HEIGHT,
    iconWidth: Dp = SELECTABLE_ITEM_ICON_WIDTH,
    onClick: (() -> Unit)?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height = height)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Box(
            modifier = Modifier.size(
                width = iconWidth,
                height = height
            ),
            contentAlignment = Alignment.Center
        ) {
            leading()
        }
        Text(
            text = text,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp, end = 8.dp)
        )
    }
}
