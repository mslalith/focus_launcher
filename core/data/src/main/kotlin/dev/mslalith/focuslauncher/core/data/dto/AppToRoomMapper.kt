package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.core.model.App

internal class AppToRoomMapper : Mapper<AppRoom, App> {
    override fun fromEntity(data: AppRoom) = App(
        name = data.name,
        displayName = data.displayName,
        packageName = data.packageName,
        isSystem = data.isSystem
    )

    override fun toEntity(data: App) = AppRoom(
        name = data.name,
        displayName = data.displayName,
        packageName = data.packageName,
        isSystem = data.isSystem
    )
}
