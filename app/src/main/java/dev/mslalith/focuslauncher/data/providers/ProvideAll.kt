package dev.mslalith.focuslauncher.data.providers

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.mslalith.focuslauncher.data.managers.LauncherViewManager
import dev.mslalith.focuslauncher.data.utils.UpdateManager

@Composable
fun ProvideAll(
    updateManager: UpdateManager,
    content: @Composable () -> Unit,
) {
    ProviderWindowInsetsLocal {
        ProvideNavController {
            ProvideSystemUiController {
                ProvideUpdateManager(updateManager) {
                    ProvideBottomSheetManager {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProviderWindowInsetsLocal(
    windowInsetsAnimationsEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    ProvideWindowInsets(windowInsetsAnimationsEnabled = windowInsetsAnimationsEnabled) {
        content()
    }
}

@Composable
private fun ProvideNavController(
    content: @Composable () -> Unit,
) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        content()
    }
}

@Composable
private fun ProvideSystemUiController(
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    CompositionLocalProvider(LocalSystemUiController provides systemUiController) {
        content()
    }
}

@Composable
private fun ProvideUpdateManager(
    updateManager: UpdateManager,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalUpdateManager provides updateManager) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProvideBottomSheetManager(
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val viewManager = remember {
        LauncherViewManager(
            coroutineScope = coroutineScope,
            scaffoldState = scaffoldState,
            bottomSheetState = modalBottomSheetState,
        )
    }
    CompositionLocalProvider(LocalLauncherViewManager provides viewManager) {
        content()
    }
}
