package baselineprofile.extensions

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2

internal val UiDevice.rootObject: UiObject2
    get() = findObject(By.scrollable(true))
