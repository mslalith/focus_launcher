package dev.mslalith.focuslauncher.feature.settingspage.settingsitems

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import dev.mslalith.focuslauncher.core.ui.OnLifecycleEventChange
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem

@Composable
internal fun SetAsDefaultLauncher(
    isDefaultLauncher: Boolean,
    refreshIsDefaultLauncher: () -> Unit
) {
    val context = LocalContext.current

    val launchForHome = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        refreshIsDefaultLauncher()
    }

    fun askToSetAsDefaultLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
            launchForHome.launch(intent)
        } else {
            context.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
        }
    }

    OnLifecycleEventChange { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            refreshIsDefaultLauncher()
        }
    }

    AnimatedVisibility(
        visible = !isDefaultLauncher,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        SettingsItem(text = "Set as Default Launcher") {
            askToSetAsDefaultLauncher()
        }
    }
}
