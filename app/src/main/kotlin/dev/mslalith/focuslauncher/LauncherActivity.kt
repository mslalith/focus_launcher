package dev.mslalith.focuslauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dagger.hilt.android.AndroidEntryPoint
import dev.mslalith.focuslauncher.core.domain.PackageActionUseCase
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import dev.mslalith.focuslauncher.core.screens.LauncherScreen
import dev.mslalith.focuslauncher.core.ui.effects.PackageActionListener
import dev.mslalith.focuslauncher.core.ui.providers.ProvideNavController
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController
import dev.mslalith.focuslauncher.feature.theme.LauncherTheme
import dev.mslalith.focuslauncher.feature.theme.LauncherThemePresenter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@IgnoreInKoverReport
class LauncherActivity : ComponentActivity() {

    @Inject
    lateinit var packageActionUseCase: PackageActionUseCase

    @Inject
    lateinit var circuit: Circuit

    @Inject
    lateinit var launcherThemePresenter: LauncherThemePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PackageActionListener { packageAction ->
                lifecycleScope.launch { packageActionUseCase(packageAction = packageAction) }
            }

            val backstack = rememberSaveableBackStack { push(LauncherScreen) }
            val navigator = rememberCircuitNavigator(backstack = backstack)

            ProvideSystemUiController {
                ProvideNavController {
                    LauncherTheme(
                        currentTheme = launcherThemePresenter.present().theme
                    ) {
                        Surface {
                            CircuitCompositionLocals(circuit = circuit) {
                                ContentWithOverlays {
                                    NavigableCircuitContent(
                                        navigator = navigator,
                                        backstack = backstack
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
