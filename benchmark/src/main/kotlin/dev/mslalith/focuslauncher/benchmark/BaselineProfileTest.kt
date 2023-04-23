package dev.mslalith.focuslauncher.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.mslalith.focuslauncher.benchmark.extensions.gotoAppDrawer
import dev.mslalith.focuslauncher.benchmark.extensions.gotoHomeFromAppDrawer
import dev.mslalith.focuslauncher.benchmark.extensions.gotoHomeFromSettings
import dev.mslalith.focuslauncher.benchmark.extensions.gotoSettings
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalBaselineProfilesApi::class)
@RunWith(AndroidJUnit4::class)
class BaselineProfileTest {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startUp() = baselineProfileRule.collectBaselineProfile(
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
