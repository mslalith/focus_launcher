package dev.mslalith.focuslauncher.extensions

import android.annotation.SuppressLint
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Telephony
import android.telecom.TelecomManager
import dev.mslalith.focuslauncher.data.model.App
import java.lang.reflect.Method

val Context.appDrawerApps: List<App>
    get() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val list = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA)
        return list.map { resolveInfo ->
            with(resolveInfo.activityInfo) {
                val appName = loadLabel(packageManager).toString()
                val appPackageName = packageName
                val isSystem = isSystemApp(appPackageName)
                return@map App(appName, appPackageName, isSystem)
            }
        }.sortedBy { it.name }
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

val Context.isOnline: Boolean
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        val allNetworkConnectionTypes = listOf(
            NetworkCapabilities.TRANSPORT_WIFI,
            NetworkCapabilities.TRANSPORT_CELLULAR,
            NetworkCapabilities.TRANSPORT_ETHERNET
        )
        return allNetworkConnectionTypes.any { networkCapabilities.hasTransport(it) }
    }

fun Context.iconOf(packageName: String): Drawable? = try {
    packageManager.getApplicationIcon(packageName)
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

fun Context.canLaunch(packageName: String): Boolean {
    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

    val list = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA)
    return list.any { resolveInfo ->
        packageName == resolveInfo?.activityInfo?.packageName
    }
}

fun Context.appNoIconModelOf(packageName: String): App? = try {
    with(packageManager) {
        val info = getApplicationInfo(packageName, 0)
        val name = getApplicationLabel(info).toString()
        val isSystem = isSystemApp(packageName)
        App(name, packageName, isSystem)
    }
} catch (ex: PackageManager.NameNotFoundException) {
    null
}

@SuppressLint("WrongConstant")
fun Context.openNotificationShade() {
    val statusBarService = getSystemService("statusbar")
    val statusBarManager: Class<*> = Class.forName("android.app.StatusBarManager")
    val method: Method = statusBarManager.getMethod("expandNotificationsPanel")
    method.invoke(statusBarService)
}

fun Context.isAppDefaultLauncher(): Boolean {
    val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
    val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return resolveInfo?.let { it.activityInfo.packageName == packageName } ?: false
}

fun Context.isDefaultLauncher(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
    roleManager.isRoleAvailable(RoleManager.ROLE_HOME) && roleManager.isRoleHeld(RoleManager.ROLE_HOME)
} else {
    isAppDefaultLauncher()
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
