package dev.mslalith.focuslauncher.feature.settingspage

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

internal fun Context.isDefaultLauncher(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
    roleManager.isRoleAvailable(RoleManager.ROLE_HOME) && roleManager.isRoleHeld(RoleManager.ROLE_HOME)
} else {
    isAppDefaultLauncher()
}

private fun Context.isAppDefaultLauncher(): Boolean {
    val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
    val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return resolveInfo?.let { it.activityInfo.packageName == packageName } ?: false
}
