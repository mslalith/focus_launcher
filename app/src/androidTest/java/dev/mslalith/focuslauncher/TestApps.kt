package dev.mslalith.focuslauncher

import dev.mslalith.focuslauncher.data.App

object TestApps {
    val Chrome = App(name = "Chrome", "com.android.chrome", isSystem = false)
    val Youtube = App(name = "Youtube", "com.android.youtube", isSystem = false)
    val Phone = App(name = "Phone", "com.android.phone", isSystem = true)

    val all = listOf(Chrome, Youtube, Phone)
}
