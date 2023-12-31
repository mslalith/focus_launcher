package dev.mslalith.focuslauncher.feature.theme.bottomsheet.themeselection

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.model.Theme
import dev.mslalith.focuslauncher.core.screens.ThemeSelectionBottomSheetScreen
import dev.mslalith.focuslauncher.core.ui.Blob
import dev.mslalith.focuslauncher.core.ui.extensions.string
import dev.mslalith.focuslauncher.feature.theme.R
import dev.mslalith.focuslauncher.feature.theme.model.ThemeWithIcon
import kotlinx.collections.immutable.ImmutableList

@CircuitInject(ThemeSelectionBottomSheetScreen::class, SingletonComponent::class)
@Composable
fun ThemeSelectionBottomSheet(
    state: ThemeSelectionBottomSheetState,
    modifier: Modifier = Modifier
) {
    // Need to extract the eventSink out to a local val, so that the Compose Compiler
    // treats it as stable. See: https://issuetracker.google.com/issues/256100927
    val eventSink = state.eventSink

    ThemeSelectionBottomSheet(
        modifier = modifier,
        currentTheme = state.currentTheme,
        allThemes = state.allThemes,
        onThemeSelected = { eventSink(ThemeSelectionBottomSheetUiEvent.SelectedTheme(theme = it)) }
    )
}

@Composable
private fun ThemeSelectionBottomSheet(
    currentTheme: Theme,
    allThemes: ImmutableList<ThemeWithIcon>,
    onThemeSelected: (Theme?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = allThemes,
            key = { it.theme }
        ) { themeWithIcon ->
            ThemeSelectionListItem(
                theme = themeWithIcon.theme,
                iconRes = themeWithIcon.iconRes,
                isSelected = themeWithIcon.theme == currentTheme,
                onClick = { onThemeSelected(if (currentTheme != themeWithIcon.theme) themeWithIcon.theme else null) }
            )
        }
    }
}

@Composable
private fun ThemeSelectionListItem(
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
