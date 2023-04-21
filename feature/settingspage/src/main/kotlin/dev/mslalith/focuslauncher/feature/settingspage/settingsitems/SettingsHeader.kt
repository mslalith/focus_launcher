package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            text = "Settings",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        Divider(
            modifier = Modifier.fillMaxWidth(fraction = 0.5f)
        )
    }
}
