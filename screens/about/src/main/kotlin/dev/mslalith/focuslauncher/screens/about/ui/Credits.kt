package dev.mslalith.focuslauncher.screens.about.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.resources.R
import dev.mslalith.focuslauncher.core.ui.TextIconButton
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer

@Composable
internal fun Credits(
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.credits),
            style = MaterialTheme.typography.titleLarge
        )
        VerticalSpacer(spacing = 12.dp)
        TextIconButton(
            icon = R.drawable.ic_logo_phosphor,
            text = stringResource(id = R.string.phosphor_icons),
            onClick = { uriHandler.openUri(uri = "https://phosphoricons.com/") },
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
