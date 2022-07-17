package dev.mslalith.focuslauncher.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mslalith.focuslauncher.data.providers.LocalNavController
import dev.mslalith.focuslauncher.ui.viewmodels.SettingsViewModel
import dev.mslalith.focuslauncher.ui.views.AppBarWithBackIcon

@Composable
fun PickPlaceForLunarPhaseScreen(
    settingsViewModel: SettingsViewModel
) {
    val navController = LocalNavController.current
    val scaffoldState = rememberScaffoldState()

    fun goBack() = navController.popBackStack()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            AppBarWithBackIcon(
                title = "Pick a Place",
                onBackPressed = { goBack() }
            )
        },
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            Text(text = "Pick a Place")
        }
    }
}
