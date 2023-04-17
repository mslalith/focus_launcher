package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.model.IconType

@Composable
fun RoundIcon(
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp,
    iconType: IconType,
    iconFraction: Float = 0.5f,
    contentDescription: String = "",
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.background,
    iconColor: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit
) {
    val animatedIconColor by animateColorAsState(
        targetValue = if (enabled) iconColor else iconColor.copy(alpha = 0.4f),
        label = "Animated Icon Color"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size = iconSize)
            .clip(shape = CircleShape)
            .background(color = backgroundColor)
            .clickable { onClick() }
    ) {
        when (iconType) {
            is IconType.Resource -> {
                Icon(
                    painter = painterResource(id = iconType.resId),
                    contentDescription = contentDescription,
                    tint = animatedIconColor,
                    modifier = Modifier.fillMaxSize(fraction = iconFraction)
                )
            }
            is IconType.Vector -> {
                Icon(
                    imageVector = iconType.imageVector,
                    contentDescription = contentDescription,
                    tint = animatedIconColor,
                    modifier = Modifier.fillMaxSize(fraction = iconFraction)
                )
            }
        }
    }
}

@Preview(name = "Enabled")
@Composable
private fun PreviewRoundIconEnabled() {
    MaterialTheme {
        RoundIcon(
            iconType = IconType.Resource(resId = R.drawable.ic_check),
            enabled = true,
            onClick = { }
        )
    }
}

@Preview(name = "Disabled")
@Composable
private fun PreviewRoundIconDisabled() {
    MaterialTheme {
        RoundIcon(
            iconType = IconType.Resource(resId = R.drawable.ic_check),
            enabled = false,
            onClick = { }
        )
    }
}
