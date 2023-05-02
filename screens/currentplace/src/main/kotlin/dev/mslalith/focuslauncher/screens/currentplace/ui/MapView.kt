package dev.mslalith.focuslauncher.screens.currentplace.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.model.location.LatLng
import dev.mslalith.focuslauncher.screens.currentplace.R
import dev.mslalith.focuslauncher.screens.currentplace.ui.interop.AndroidMapView

@Composable
internal fun MapView(
    modifier: Modifier = Modifier,
    initialLatLngProvider: suspend () -> LatLng,
    onLocationChange: (LatLng) -> Unit
) {
    Box(modifier = modifier) {
        AndroidMapView(
            initialLatLngProvider = initialLatLngProvider,
            onLocationChange = onLocationChange
        )
        MapOverlay()
    }
}

@Composable
private fun BoxScope.MapOverlay() {
    Box(
        modifier = Modifier.matchParentSize()
    ) {
        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 4.dp, end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            MapCredits()
        }
    }
}

@Composable
private fun MapCredits() {
    val uriHandler = LocalUriHandler.current

    val openStreetMapText = stringResource(id = R.string.openstreetmap)
    val openStreetMapCredits = stringResource(id = R.string.openstreetmap_contributors, openStreetMapText)

    val annotatedString = remember {
        buildAnnotatedString {
            val start = openStreetMapCredits.indexOf(openStreetMapText)
            val end = start + openStreetMapText.length

            append(openStreetMapCredits)
            addStyle(
                style = SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                ),
                start = start,
                end = end
            )
            addStringAnnotation(
                tag = "url",
                annotation = "https://www.openstreetmap.org/copyright",
                start = start,
                end = end
            )
        }
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            val url = annotatedString.getStringAnnotations(tag = "url", start = offset, end = offset).firstOrNull()?.item
            if (url != null) uriHandler.openUri(url)
        }
    )
}
