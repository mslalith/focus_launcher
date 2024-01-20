package dev.mslalith.focuslauncher.screens.about.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import dev.mslalith.focuslauncher.core.resources.R
import dev.mslalith.focuslauncher.core.ui.VerticalSpacer

@Composable
internal fun AppInfo(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val appIcon = remember { context.appIcon() }
    val version = remember { context.versionName() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = appIcon,
            contentDescription = stringResource(id = R.string.app_icon)
        )
        VerticalSpacer(spacing = 8.dp)
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )
        VerticalSpacer(spacing = 2.dp)
        Text(
            text = version,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium
        )
        VerticalSpacer(spacing = 8.dp)
        SocialAccounts()
    }
}

@IgnoreInKoverReport
private fun Context.appIcon(): ImageBitmap = packageManager.getApplicationIcon(packageName).toBitmap().asImageBitmap()

@IgnoreInKoverReport
private fun Context.versionName(): String {
    val versionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0)).versionName
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0).versionName
    }
    return if (versionName.isNullOrEmpty()) "-" else "v".plus(versionName)
}
