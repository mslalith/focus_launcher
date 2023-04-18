package dev.mslalith.focuslauncher.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SelectableIconItem(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconRes: Int,
    contentDescription: String? = null,
    height: Dp = 48.dp,
    iconWidth: Dp = 56.dp,
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
            )
        }
    )
}
