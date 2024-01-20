package dev.mslalith.focuslauncher.core.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import dev.mslalith.focuslauncher.core.model.app.App
import java.lang.reflect.Method

@IgnoreInKoverReport
@SuppressLint("WrongConstant")
fun Context.openNotificationShade() {
    val statusBarService = getSystemService("statusbar")
    val statusBarManager: Class<*> = Class.forName("android.app.StatusBarManager")
    val method: Method = statusBarManager.getMethod("expandNotificationsPanel")
    method.invoke(statusBarService)
}

@IgnoreInKoverReport
fun Context.launchApp(app: App) {
    packageManager.getLaunchIntentForPackage(app.packageName)?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(it)
    }
}

@IgnoreInKoverReport
fun Context.showAppInfo(packageName: String) {
    with(Intent()) {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
        startActivity(this)
    }
}

@IgnoreInKoverReport
fun Context.uninstallApp(app: App) {
    with(Intent(Intent.ACTION_DELETE)) {
        data = Uri.parse("package:${app.packageName}")
        startActivity(this)
    }
}
