package dev.mslalith.focuslauncher.screens.editfavorites.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.screens.editfavorites.utils.TestTags

@Composable
internal fun HiddenAppActionText(
    showHiddenApps: Boolean,
    onToggleHiddenApps: (Boolean) -> Unit
) {
    val textDecoration = when (showHiddenApps) {
        true -> TextDecoration.None
        false -> TextDecoration.LineThrough
    }

    TextButton(
        onClick = { onToggleHiddenApps(!showHiddenApps) },
        modifier = Modifier
            .testSemantics(tag = TestTags.TAG_TOGGLE_HIDDEN_APPS)
            .padding(end = 8.dp)
    ) {
        Text(
            text = " Hidden Apps ",
            textDecoration = textDecoration,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
