package dev.mslalith.focuslauncher.core.testing

import dev.mslalith.focuslauncher.core.model.App

object TestApps {
    val Chrome = App(name = "Chrome", packageName = "com.android.chrome", isSystem = true)
    val Youtube = App(name = "Youtube", packageName = "com.android.youtube", isSystem = false)
    val Phone = App(name = "Phone", packageName = "com.android.phone", isSystem = true)

    val all = listOf(Chrome, Youtube, Phone)
}
