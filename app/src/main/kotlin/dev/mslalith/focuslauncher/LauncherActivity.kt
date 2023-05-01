package dev.mslalith.focuslauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.mslalith.focuslauncher.core.domain.PackageActionUseCase
import dev.mslalith.focuslauncher.core.domain.theme.GetThemeUseCase
import dev.mslalith.focuslauncher.core.model.Constants.Defaults.Settings.General.DEFAULT_THEME
import dev.mslalith.focuslauncher.core.ui.effects.PackageActionListener
import dev.mslalith.focuslauncher.core.ui.providers.ProvideNavController
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.navigator.AppNavigator
import dev.mslalith.focuslauncher.ui.theme.FocusLauncherTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {

    @Inject
    lateinit var packageActionUseCase: PackageActionUseCase

    @Inject
    lateinit var getThemeUseCase: GetThemeUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            PackageActionListener { packageAction ->
                lifecycleScope.launch { packageActionUseCase(packageAction = packageAction) }
            }

            ProvideSystemUiController {
                ProvideNavController {
                    FocusLauncherTheme(
                        currentTheme = getThemeUseCase().collectAsStateWithLifecycle(initialValue = DEFAULT_THEME).value
                    ) {
                        Surface {
                            AppNavigator()
                        }
                    }
                }
            }
        }
    }
}
