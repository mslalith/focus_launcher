package dev.mslalith.focuslauncher.data.providers

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.SystemUiController
import dev.mslalith.focuslauncher.data.managers.LauncherViewManager
import dev.mslalith.focuslauncher.data.utils.UpdateManager

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No NavHostController provided")
}

val LocalSystemUiController = staticCompositionLocalOf<SystemUiController> {
    error("No SystemUiController provided")
}

val LocalLauncherViewManager = staticCompositionLocalOf<LauncherViewManager> {
    error("No LauncherViewManager provided")
}

val LocalUpdateManager = staticCompositionLocalOf<UpdateManager> {
    error("No UpdateManager provided")
}
