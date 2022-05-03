package dev.mslalith.focuslauncher

import dev.mslalith.focuslauncher.data.database.entities.AppRoom

object TestApps {
    val Chrome = AppRoom(name = "Chrome", "com.android.chrome", isSystem = false)
    val Youtube = AppRoom(name = "Youtube", "com.android.youtube", isSystem = false)
    val Phone = AppRoom(name = "Phone", "com.android.phone", isSystem = true)

    val all = listOf(Chrome, Youtube, Phone)
}
