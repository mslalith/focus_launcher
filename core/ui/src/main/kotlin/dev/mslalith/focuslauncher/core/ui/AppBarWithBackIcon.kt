package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AppBarWithBackIcon(
    title: String,
    onBackPressed: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val colors = MaterialTheme.colors

    TopAppBar(
        backgroundColor = colors.background,
        contentColor = colors.onBackground,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = colors.onBackground
                )
            }
        },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colors.onBackground,
                fontSize = MaterialTheme.typography.h6.fontSize
            )
        },
        actions = actions
    )
}
