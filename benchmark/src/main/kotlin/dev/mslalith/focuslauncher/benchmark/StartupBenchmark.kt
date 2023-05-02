package dev.mslalith.focuslauncher.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.mslalith.focuslauncher.benchmark.extensions.gotoAppDrawer
import dev.mslalith.focuslauncher.benchmark.extensions.gotoHomeFromAppDrawer
import dev.mslalith.focuslauncher.benchmark.extensions.gotoHomeFromSettings
import dev.mslalith.focuslauncher.benchmark.extensions.gotoSettings
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class StartupBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startUpCompilationModeNone() = startUp(compilationMode = CompilationMode.None())

    @Test
    fun startUpCompilationModePartial() = startUp(compilationMode = CompilationMode.Partial())

    @Test
    fun startUpCompilationModeFull() = startUp(compilationMode = CompilationMode.Full())

    private fun startUp(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "dev.mslalith.focuslauncher",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = compilationMode
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
