package dev.mslalith.focuslauncher.screens.iconpack.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.core.ui.extensions.clickableNoRipple
import dev.mslalith.focuslauncher.core.ui.model.AppWithIcon

internal val APP_ICON_SIZE = 28.dp

@Composable
internal fun IconPackItem(
    modifier: Modifier = Modifier,
    app: AppWithIcon,
    isSelected: Boolean,
    onClick: (AppWithIcon) -> Unit,
) {
    val iconBitmap = remember(key1 = app.packageName) {
        app.icon.toBitmap().asImageBitmap()
    }

    val backgroundColorAlpha by animateFloatAsState(
        label = "Background Color",
        targetValue = if (isSelected) 1f else 0f
    )

    Column(
        modifier = modifier
            .width(intrinsicSize = IntrinsicSize.Min)
            .padding(horizontal = 4.dp, vertical = 0.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colors.secondaryVariant.copy(alpha = backgroundColorAlpha))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickableNoRipple { onClick(app) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var appName by remember { mutableStateOf(value = app.displayName) }

        Box(
            modifier = Modifier.width(width = APP_ICON_SIZE * 1.5f)
        ) {
            Image(
                bitmap = iconBitmap,
                contentDescription = app.displayName,
            )
        }
        VerticalSpacer(spacing = 8.dp)
        Text(
            text = appName,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp
            ),
            modifier = Modifier.animateContentSize(),
            onTextLayout = { result ->
                val emptyLinesCount = result.multiParagraph.run { maxLines - lineCount }
                if (emptyLinesCount > 0) {
                    appName = appName.plus("\n".repeat(n = emptyLinesCount))
                }
            }
        )
    }
}
