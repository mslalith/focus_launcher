package dev.mslalith.focuslauncher.screens.about.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.screens.about.R

@Composable
internal fun MadeWithLove(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    style: TextStyle = MaterialTheme.typography.bodySmall
) {
    Row(
        modifier = modifier.padding(paddingValues = paddingValues),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Made with",
            style = style
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_heart),
            contentDescription = "Love icon",
            tint = Color.Red,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .size(size = style.fontSize.value.dp + 4.dp)
        )
        Text(
            text = "from India",
            style = style
        )
    }
}

@Preview
@Composable
private fun PreviewMadeWithLove() {
    Surface {
        MadeWithLove()
    }
}
