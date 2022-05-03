package dev.mslalith.focuslauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.view.WindowCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import dev.mslalith.focuslauncher.data.utils.UpdateManager
import dev.mslalith.focuslauncher.data.providers.ProvideAll
import dev.mslalith.focuslauncher.extensions.appNoIconModelOf
import dev.mslalith.focuslauncher.extensions.canLaunch
import dev.mslalith.focuslauncher.navigator.AppNavigator
import dev.mslalith.focuslauncher.ui.theme.FocusLauncherTheme
import dev.mslalith.focuslauncher.ui.viewmodels.AppsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {

    @Inject
    lateinit var mUpdateManager: UpdateManager

    private val mAppsViewModel by viewModels<AppsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycle.addObserver(launcherLifeCycleObserver)

        mAppsViewModel.setAppsIfCacheEmpty(context = this, checkCache = false)

        setContent {
            ProvideAll(
                updateManager = mUpdateManager,
            ) {
                FocusLauncherTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        AppNavigator()
                    }
                }
            }
        }
    }

    private val launcherLifeCycleObserver = object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) = registerPackageAddedOrRemovedReceiver()
        override fun onDestroy(owner: LifecycleOwner) = unregisterPackageAddedOrRemovedReceiver()
    }

    private val packageAddedOrRemovedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
                intent.dataString?.substringAfter(delimiter = "package:")?.let { packageName ->
                    mAppsViewModel.handleAppUninstall(packageName)
                }
            } else if (action == Intent.ACTION_PACKAGE_ADDED) {
                intent.dataString?.substringAfter(delimiter = "package:")?.let { packageName ->
                    if (context.canLaunch(packageName)) {
                        context.appNoIconModelOf(packageName)?.let { app ->
                            mAppsViewModel.handleAppInstall(app)
                        }
                    }
                }
            }
        }
    }

    fun registerPackageAddedOrRemovedReceiver() {
        val intent = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
            addDataScheme("package")
        }
        registerReceiver(packageAddedOrRemovedReceiver, intent)
    }

    fun unregisterPackageAddedOrRemovedReceiver() = unregisterReceiver(packageAddedOrRemovedReceiver)
}
