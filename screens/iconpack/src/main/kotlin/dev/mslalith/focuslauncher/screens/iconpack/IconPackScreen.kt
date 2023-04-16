package dev.mslalith.focuslauncher.screens.iconpack

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon

@Composable
fun IconPackScreen(
    goBack: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackIcon(
                title = "Icon Pack",
                onBackPressed = goBack
            )
        }
    ) {
        Box(modifier = Modifier.padding(paddingValues = it)) {
            Text(
                text = "Icon Pack",
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}
