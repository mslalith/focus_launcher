package dev.mslalith.focuslauncher.benchmark.extensions

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2

val UiDevice.rootObject: UiObject2
    get() = findObject(By.scrollable(true))
