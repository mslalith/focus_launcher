package dev.mslalith.focuslauncher.core.settings.sentry

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import io.sentry.android.core.SentryAndroid
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SentrySettingsImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    generalSettingsRepo: GeneralSettingsRepo
) : SentrySettings {

    override val isEnabled: Flow<Boolean> = generalSettingsRepo.reportCrashesFlow

    override fun enableSentry() {
        SentryAndroid.init(context) { it.dsn = getSentryDsn() }
    }

    override fun disableSentry() {
        SentryAndroid.init(context) { it.dsn = "" }
    }

    override fun getSentryDsn(): String = try {
        val applicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getApplicationInfo(context.packageName, PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
        } else {
            context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        }
        applicationInfo.metaData.getString("dev.mslalith.focuslauncher.sentryDsn", "")
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}
