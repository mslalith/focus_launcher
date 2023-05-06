package dev.mslalith.focuslauncher.core.model.app

import android.content.ComponentName

data class AppWithComponent(
    val app: App,
    val componentName: ComponentName
)
