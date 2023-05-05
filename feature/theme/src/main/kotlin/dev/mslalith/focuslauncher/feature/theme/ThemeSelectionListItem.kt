package dev.mslalith.focuslauncher.feature.theme

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.ui.Blob
import dev.mslalith.focuslauncher.core.ui.extensions.string

@Composable
internal fun ThemeSelectionListItem(
    theme: Theme,
    @DrawableRes iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(text = theme.uiText.string()) },
        leadingContent = @Composable {
            Blob(
                size = 48.dp,
                numberOfPoints = 6,
                color = MaterialTheme.colorScheme.primary,
                content = @Composable {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        },
        trailingContent = if (isSelected) {
            @Composable {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = ""
                )
            }
        } else null
    )
}
