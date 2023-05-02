package dev.mslalith.focuslauncher.screens.about.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.resources.R

@Composable
internal fun SocialAccounts() {
    val uriHandler = LocalUriHandler.current

    Row {
        SocialAccount(
            iconRes = R.drawable.ic_logo_twitter,
            contentDescription = stringResource(id = R.string.twitter_icon),
            onClick = { uriHandler.openUri(uri = "https://twitter.com/mslalith") }
        )
        SocialAccount(
            iconRes = R.drawable.ic_logo_github,
            contentDescription = stringResource(id = R.string.github_icon),
            onClick = { uriHandler.openUri(uri = "https://github.com/mslalith") }
        )
    }
}

@Composable
private fun SocialAccount(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    size: Dp = 16.dp,
    outerPadding: Dp = 4.dp,
    innerPadding: Dp = 4.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = outerPadding)
            .clip(shape = CircleShape)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(all = innerPadding)
                .size(size = size)
                .clickable { onClick() }
        )
    }
}
