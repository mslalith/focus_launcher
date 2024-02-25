package dev.mslalith.focuslauncher.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.mslalith.focuslauncher.core.model.UiText

@Composable
fun UiText.string(): String = when (this) {
    is UiText.Static -> text
    is UiText.Resource -> stringResource(id = stringRes)
}
