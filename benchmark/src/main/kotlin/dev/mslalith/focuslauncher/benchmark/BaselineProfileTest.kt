package dev.mslalith.focuslauncher.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.mslalith.focuslauncher.benchmark.extensions.gotoAppDrawer
import dev.mslalith.focuslauncher.benchmark.extensions.gotoHomeFromAppDrawer
import dev.mslalith.focuslauncher.benchmark.extensions.gotoHomeFromSettings
import dev.mslalith.focuslauncher.benchmark.extensions.gotoSettings
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RequiresApi(Build.VERSION_CODES.P)
@RunWith(AndroidJUnit4::class)
internal class BaselineProfileTest {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startUp() = baselineProfileRule.collect(
        packageName = "dev.mslalith.focuslauncher"
    ) {
        pressHome()
        startActivityAndWait()
        device.waitForIdle(5_000)
        gotoSettings()
        gotoHomeFromSettings()
        gotoAppDrawer()
        gotoHomeFromAppDrawer()
    }
}
