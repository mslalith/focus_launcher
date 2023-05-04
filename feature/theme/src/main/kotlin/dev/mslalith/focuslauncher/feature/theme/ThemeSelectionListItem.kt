package dev.mslalith.focuslauncher.feature.theme

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.ui.extensions.string

@Composable
internal fun ThemeSelectionListItem(
    theme: Theme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(text = theme.uiText.string()) },
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
