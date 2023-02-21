package dev.mslalith.focuslauncher.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
    val colors = MaterialTheme.colors

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues = paddingValues),
        value = query,
        textStyle = TextStyle(color = colors.onBackground),
        onValueChange = { onQueryChange(it) },
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(color = colors.onBackground)
            )
        },
        shape = MaterialTheme.shapes.small,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Search
        ),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = colors.onBackground,
            backgroundColor = colors.primaryVariant,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(Icons.Rounded.Search, contentDescription = "Search")
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onQueryChange("") }
                        .padding(4.dp)
                ) {
                    Icon(
                        Icons.Rounded.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )
}
