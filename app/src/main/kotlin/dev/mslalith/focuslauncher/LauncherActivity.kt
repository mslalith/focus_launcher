package dev.mslalith.focuslauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.mslalith.focuslauncher.core.domain.favorites.PackageActionUseCase
import dev.mslalith.focuslauncher.core.ui.effects.PackageActionListener
import dev.mslalith.focuslauncher.core.ui.providers.ProvideBottomSheetManager
import dev.mslalith.focuslauncher.core.ui.providers.ProvideNavController
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.navigator.AppNavigator
import dev.mslalith.focuslauncher.ui.theme.FocusLauncherTheme
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {

    @Inject
    lateinit var packageActionUseCase: PackageActionUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            PackageActionListener { packageAction ->
                lifecycleScope.launch { packageActionUseCase(packageAction = packageAction) }
            }

            ProvideSystemUiController {
                ProvideNavController {
                    FocusLauncherTheme {
                        ProvideBottomSheetManager {
                            Surface {
                                AppNavigator()
                            }
                        }
                    }
                }
            }
        }
    }
}
