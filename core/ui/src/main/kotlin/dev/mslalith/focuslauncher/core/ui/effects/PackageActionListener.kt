package dev.mslalith.focuslauncher.core.ui.effects

import android.content.pm.LauncherApps
import android.os.UserHandle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import dev.mslalith.focuslauncher.core.model.PackageAction

@Composable
fun PackageActionListener(
    onAction: (PackageAction) -> Unit
) {
    val context = LocalContext.current
    val updatedOnAction by rememberUpdatedState(newValue = onAction)

    DisposableEffect(key1 = context, key2 = updatedOnAction) {
        val launcherApps = context.getSystemService(LauncherApps::class.java)
        val callback = object : LauncherApps.Callback() {
            override fun onPackageRemoved(packageName: String?, user: UserHandle?) {
                packageName ?: return
                updatedOnAction(PackageAction.Removed(packageName))
            }

            override fun onPackageAdded(packageName: String?, user: UserHandle?) {
                packageName ?: return
                updatedOnAction(PackageAction.Added(packageName))
            }

            override fun onPackageChanged(packageName: String?, user: UserHandle?) {
                packageName ?: return
                updatedOnAction(PackageAction.Updated(packageName))
            }

            override fun onPackagesAvailable(packageNames: Array<out String>?, user: UserHandle?, replacing: Boolean) = Unit
            override fun onPackagesUnavailable(packageNames: Array<out String>?, user: UserHandle?, replacing: Boolean) = Unit
        }

        launcherApps.registerCallback(callback)

        onDispose { launcherApps.unregisterCallback(callback) }
    }
}
