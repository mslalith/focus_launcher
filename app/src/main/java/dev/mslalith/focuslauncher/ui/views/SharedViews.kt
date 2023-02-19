package dev.mslalith.focuslauncher.ui.views

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mslalith.focuslauncher.core.ui.HorizontalSpacer

@Composable
fun BackPressHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    LocalOnBackPressedDispatcherOwner.current?.let {
        val backPressedDispatcher = it.onBackPressedDispatcher
        DisposableEffect(enabled) {
            when (enabled) {
                true -> backPressedDispatcher.addCallback(backPressedCallback)
                false -> backPressedCallback.remove()
            }
            onDispose { backPressedCallback.remove() }
        }
    }
}

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
        title = { Text(text = title) },
        actions = actions
    )
}

@Composable
fun ExtendedMiniFab(
    text: String,
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colors

    Row(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(percent = 50))
            .clickable { onClick() }
            .background(colors.onBackground.copy(alpha = 0.85f))
            .padding(horizontal = 12.dp)
    ) {
        val centerVerticallyModifier = Modifier.align(Alignment.CenterVertically)

        Icon(
            imageVector = icon,
            contentDescription = contentDescription ?: text,
            tint = colors.background,
            modifier = centerVerticallyModifier.padding(vertical = 10.dp)
        )
        Text(
            text = text,
            style = TextStyle(
                color = colors.background,
                fontSize = 13.sp
            ),
            modifier = centerVerticallyModifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = MaterialTheme.colors.secondaryVariant,
    textColor: Color = MaterialTheme.colors.onBackground,
    paddingValues: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .clickable { onClick() }
            .padding(paddingValues = paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = textColor
            )
        )
    }
}

@Composable
fun ChooserGroup(
    modifier: Modifier = Modifier,
    textIconsList: List<Pair<String, Int>>,
    selectedItem: String,
    onItemSelected: (Int) -> Unit
) {
    val selectedColor = MaterialTheme.colors.secondaryVariant

    Row(
        modifier = modifier
    ) {
        textIconsList.forEachIndexed { index, textIcon ->
            val isSelected = selectedItem == textIcon.first
            val backgroundColorAlpha by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0f,
                animationSpec = tween(durationMillis = 300)
            )

            TextIconButton(
                text = textIcon.first,
                icon = painterResource(id = textIcon.second),
                backgroundColor = selectedColor.copy(alpha = backgroundColorAlpha),
                onClick = {
                    if (!isSelected) {
                        onItemSelected(index)
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(weight = 1f)
            )
        }
    }
}

@Composable
fun TextIconButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    contentDescription: String? = null,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = 12.dp,
        vertical = 8.dp
    ),
    afterIconSpacing: Dp = 14.dp,
    backgroundColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = backgroundColor)
            .clickable { onClick() }
            .padding(paddingValues = paddingValues)
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription ?: text,
            tint = MaterialTheme.colors.onBackground
        )
        HorizontalSpacer(spacing = afterIconSpacing)
        Text(
            text = text,
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun RoundedSwitch(
    checked: Boolean,
    enabled: Boolean = true
) {
    val thumbColor = MaterialTheme.colors.onBackground
    val trackColor = thumbColor.copy(alpha = 0.3f)
    val disabledThumbColor = thumbColor.copy(alpha = 0.2f)
    val disabledTrackColor = thumbColor.copy(alpha = 0.1f)

    val padding = 2.dp
    val thumbSize = 20.dp
    val trackWidth = (thumbSize * 1.75f) + (padding * 2)

    val horizontalBias by animateFloatAsState(targetValue = if (checked) 1f else -1f)
    val animatedThumbColor by animateColorAsState(targetValue = if (enabled) thumbColor else disabledThumbColor)
    val animatedTrackColor by animateColorAsState(targetValue = if (enabled) trackColor else disabledTrackColor)

    Box(
        modifier = Modifier
            .width(width = trackWidth)
            .clip(shape = CircleShape)
            .background(color = animatedTrackColor)
            .padding(all = padding)
            .heightIn(min = thumbSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .requiredSize(size = thumbSize)
                .clip(shape = CircleShape)
                .background(color = animatedThumbColor)
                .align(
                    alignment = BiasAlignment(
                        horizontalBias = horizontalBias,
                        verticalBias = 0f
                    )
                )
        )
    }
}
