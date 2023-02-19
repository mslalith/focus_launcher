package dev.mslalith.focuslauncher.data.providers

import androidx.compose.runtime.Composable
import dev.mslalith.focuslauncher.core.ui.providers.ProvideBottomSheetManager
import dev.mslalith.focuslauncher.core.ui.providers.ProvideNavController
import dev.mslalith.focuslauncher.core.ui.providers.ProvideSystemUiController

@Composable
fun ProvideAll(
    content: @Composable () -> Unit
) {
    ProvideNavController {
        ProvideSystemUiController {
            ProvideBottomSheetManager {
                content()
            }
        }
    }
}
