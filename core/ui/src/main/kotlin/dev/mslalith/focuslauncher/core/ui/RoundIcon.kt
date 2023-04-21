package dev.mslalith.focuslauncher.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RoundIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    contentDescription: String = "",
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    IconButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = backgroundColor),
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = contentColor
        )
    }
}

@Preview(name = "Enabled")
@Composable
private fun PreviewRoundIconEnabled() {
    Surface {
        RoundIcon(
            iconRes = R.drawable.ic_check,
            enabled = true,
            onClick = { }
        )
    }
}

@Preview(name = "Disabled")
@Composable
private fun PreviewRoundIconDisabled() {
    Surface {
        RoundIcon(
            iconRes = R.drawable.ic_check,
            enabled = false,
            onClick = { }
        )
    }
}
