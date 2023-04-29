package dev.mslalith.focuslauncher.screens.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mslalith.focuslauncher.core.ui.AppBarWithBackIcon
import dev.mslalith.focuslauncher.core.ui.FillSpacer
import dev.mslalith.focuslauncher.core.ui.StatusBarColor
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer
import dev.mslalith.focuslauncher.screens.about.ui.AppInfo
import dev.mslalith.focuslauncher.screens.about.ui.Credits
import dev.mslalith.focuslauncher.screens.about.ui.MadeWithLove

@Composable
fun AboutScreen(
    goBack: () -> Unit
) {
    StatusBarColor(hasTopAppBar = true)

    Scaffold(
        topBar = {
            AppBarWithBackIcon(
                title = "About",
                onBackPressed = goBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FillSpacer()
            AppInfo()
            FillSpacer()

            Credits()
            VerticalSpacer(spacing = 24.dp)
            MadeWithLove()
        }
    }
}
