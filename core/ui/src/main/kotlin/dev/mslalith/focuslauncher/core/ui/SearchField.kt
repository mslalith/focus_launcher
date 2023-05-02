package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    placeholder: String,
    query: String,
    onQueryChange: (String) -> Unit,
    paddingValues: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues = paddingValues),
        value = query,
        onValueChange = { onQueryChange(it) },
        placeholder = {
            Text(text = placeholder)
        },
        shape = MaterialTheme.shapes.small,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Search
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.search)
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .clickable { onQueryChange("") }
                        .padding(all = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = stringResource(id = R.string.clear)
                    )
                }
            }
        }
    )
}
