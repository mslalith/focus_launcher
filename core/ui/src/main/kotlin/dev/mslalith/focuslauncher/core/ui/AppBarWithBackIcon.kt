package dev.mslalith.focuslauncher.core.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithBackIcon(
    title: String,
    onBackPressed: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            RoundIcon(
                iconRes = R.drawable.ic_arrow_left,
                contentDescription = stringResource(id = R.string.go_back),
                onClick = onBackPressed
            )
        },
        title = {
            Text(text = title)
        },
        actions = actions
    )
}
