package dev.mslalith.focuslauncher.core.testing

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

class AppRobolectricTestRunner(testClass: Class<*>?) : RobolectricTestRunner(testClass) {

    override fun buildGlobalConfig(): Config {
        return Config.Builder()
//            .setSdk(33)
            .build()
    }
}
