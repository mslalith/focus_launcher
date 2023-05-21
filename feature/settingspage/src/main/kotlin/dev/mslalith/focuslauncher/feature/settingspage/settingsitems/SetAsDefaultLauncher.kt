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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import dev.mslalith.focuslauncher.core.testing.compose.modifier.testsemantics.testSemantics
import dev.mslalith.focuslauncher.core.ui.effects.OnLifecycleEventChange
import dev.mslalith.focuslauncher.feature.settingspage.R
import dev.mslalith.focuslauncher.feature.settingspage.shared.SettingsItem
import dev.mslalith.focuslauncher.feature.settingspage.utils.TestTags

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
        SettingsItem(
            modifier = Modifier.testSemantics(tag = TestTags.ITEM_SET_AS_DEFAULT),
            text = stringResource(id = R.string.set_as_default_launcher),
            onClick = ::askToSetAsDefaultLauncher
        )
    }
}
