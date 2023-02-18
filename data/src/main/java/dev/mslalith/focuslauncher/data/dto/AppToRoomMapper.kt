package dev.mslalith.focuslauncher.data.dto

import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.data.database.entities.AppRoom

class AppToRoomMapper : Mapper<AppRoom, App> {
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
