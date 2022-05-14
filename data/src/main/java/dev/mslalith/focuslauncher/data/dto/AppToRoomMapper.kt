package dev.mslalith.focuslauncher.data.dto

import dev.mslalith.focuslauncher.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.data.model.App

class AppToRoomMapper : Mapper<AppRoom, App> {
    override fun fromEntity(data: AppRoom) = App(
        name = data.name,
        packageName = data.packageName,
        isSystem = data.isSystem
    )

    override fun toEntity(data: App) = AppRoom(
        name = data.name,
        packageName = data.packageName,
        isSystem = data.isSystem
    )
}
