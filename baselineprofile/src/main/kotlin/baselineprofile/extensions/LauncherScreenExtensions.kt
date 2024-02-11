package baselineprofile.extensions

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until

internal fun MacrobenchmarkScope.gotoSettings() {
    device.rootObject.fling(Direction.LEFT)
    device.wait(Until.hasObject(By.text("Change Theme")), 5_000)
}

internal fun MacrobenchmarkScope.gotoHomeFromSettings() {
    device.rootObject.fling(Direction.RIGHT)
    device.wait(Until.hasObject(By.text("Full Moon")), 5_000)
}

internal fun MacrobenchmarkScope.gotoHomeFromAppDrawer() {
    device.rootObject.fling(Direction.LEFT)
    device.wait(Until.hasObject(By.text("Full Moon")), 5_000)
}

internal fun MacrobenchmarkScope.gotoAppDrawer() {
    device.rootObject.fling(Direction.RIGHT)
    device.wait(Until.hasObject(By.text("Chrome")), 5_000)
}
