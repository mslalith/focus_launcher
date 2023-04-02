package dev.mslalith.focuslauncher.core.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.provider.Telephony
import android.telecom.TelecomManager
import android.widget.Toast
import dev.mslalith.focuslauncher.core.model.App
import java.lang.reflect.Method

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

@SuppressLint("WrongConstant")
fun Context.openNotificationShade() {
    val statusBarService = getSystemService("statusbar")
    val statusBarManager: Class<*> = Class.forName("android.app.StatusBarManager")
    val method: Method = statusBarManager.getMethod("expandNotificationsPanel")
    method.invoke(statusBarService)
}

fun Context.appNoIconModelOf(packageName: String): App? = try {
    with(packageManager) {
        val info = getApplicationInfo(packageName, 0)
        val name = getApplicationLabel(info).toString()
        val isSystem = isSystemApp(packageName)
        App(
            name = name,
            displayName = name,
            packageName = packageName,
            isSystem = isSystem
        )
    }
} catch (ex: PackageManager.NameNotFoundException) {
    null
}

fun Context.isSystemApp(packageName: String) = try {
    (
        packageManager.getApplicationInfo(packageName, 0).flags and
            (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)
        ) != 0
} catch (ex: PackageManager.NameNotFoundException) {
    false
}

val Context.defaultDialerApp: App?
    get() {
        val manager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        return manager.defaultDialerPackage?.let { appNoIconModelOf(it) }
    }

val Context.defaultMessagingApp: App?
    get() {
        val packageName: String? = Telephony.Sms.getDefaultSmsPackage(this)
        return packageName?.let { appNoIconModelOf(packageName) }
    }

fun Context.launchApp(app: App) {
    packageManager.getLaunchIntentForPackage(app.packageName)?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(it)
    }
}

fun Context.showAppInfo(packageName: String) {
    with(Intent()) {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
        startActivity(this)
    }
}

fun Context.uninstallApp(app: App) {
    with(Intent(Intent.ACTION_DELETE)) {
        data = Uri.parse("package:${app.packageName}")
        startActivity(this)
    }
}
