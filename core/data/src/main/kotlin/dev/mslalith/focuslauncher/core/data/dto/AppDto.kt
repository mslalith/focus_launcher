package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.core.data.database.entities.HiddenAppRoom
import dev.mslalith.focuslauncher.core.model.App

internal fun AppRoom.toApp(): App = App(
    name = name,
    displayName = displayName,
    packageName = packageName,
    isSystem = isSystem
)

internal fun App.toAppRoom(): AppRoom = AppRoom(
    name = name,
    displayName = displayName,
    packageName = packageName,
    isSystem = isSystem
)

internal fun App.toHiddenAppRoom(): HiddenAppRoom = HiddenAppRoom(packageName = packageName)
