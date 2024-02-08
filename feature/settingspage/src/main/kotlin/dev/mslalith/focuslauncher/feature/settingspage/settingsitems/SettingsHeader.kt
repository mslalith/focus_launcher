package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.feature.settingspage.R

@Composable
internal fun SettingsHeader() {
    // this should be relative to ITEM_PADDING from SettingsItem.kt
    val usableHorizontalPadding = 24.dp.times(other = 1.3f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = usableHorizontalPadding)
    ) {
        Text(
            text = stringResource(id = R.string.settings),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(fraction = 0.5f)
        )
    }
}
