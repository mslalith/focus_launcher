package dev.mslalith.focuslauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.mslalith.focuslauncher.navigator.AppNavigator
import dev.mslalith.focuslauncher.providers.ProvideAll
import dev.mslalith.focuslauncher.ui.theme.FocusLauncherTheme

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ProvideAll {
                FocusLauncherTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        AppNavigator()
                    }
                }
            }
        }
    }
}
