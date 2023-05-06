package dev.mslalith.focuslauncher.core.launcherapps.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import java.util.Calendar

object GoogleCalendarIcon {
    const val GOOGLE_CALENDAR_PACKAGE_NAME = "com.google.android.calendar"

    fun getDrawable(context: Context, activityName: String): Drawable? {
        val packageManager = context.packageManager
        try {
            val cn = ComponentName(GOOGLE_CALENDAR_PACKAGE_NAME, activityName)
            val activityInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getActivityInfo(cn, PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
            } else {
                @Suppress("DEPRECATION")
                packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA)
            }
            val resourcesForApplication = packageManager.getResourcesForApplication(GOOGLE_CALENDAR_PACKAGE_NAME)
            val dayResId = getDayResId(activityInfo.metaData, resourcesForApplication)
            if (dayResId != 0) {
                @Suppress("DEPRECATION")
                return resourcesForApplication.getDrawable(dayResId)
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        return null
    }

    private fun getDayResId(bundle: Bundle?, resources: Resources): Int {
        bundle ?: return 0

        val dateArrayId = bundle.getInt("$GOOGLE_CALENDAR_PACKAGE_NAME.dynamic_icons_nexus_round", 0)
        if (dateArrayId == 0) return 0

        return try {
            val dateIds = resources.obtainTypedArray(dateArrayId)
            val dateId = dateIds.getResourceId(Calendar.getInstance()[Calendar.DAY_OF_MONTH] - 1, 0)
            dateIds.recycle()
            dateId
        } catch (ignored: Resources.NotFoundException) {
            0
        }
    }
}
