package dev.mslalith.focuslauncher.data.dto

import dev.mslalith.focuslauncher.core.model.App
import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.entities.HiddenAppRoom
import dev.mslalith.focuslauncher.data.di.modules.AppToRoomMapperProvider
import javax.inject.Inject

class HiddenToRoomMapper @Inject constructor(
    private val appsDao: AppsDao,
    @AppToRoomMapperProvider private val appToRoomMapper: AppToRoomMapper
) : Mapper<HiddenAppRoom, App> {
    override fun fromEntity(data: HiddenAppRoom): App {
        val app = appsDao.getAppBySync(data.packageName)
            ?: throw IllegalStateException("${data.packageName} app was not found in Database")
        return appToRoomMapper.fromEntity(app)
    }

    override fun toEntity(data: App) = HiddenAppRoom(packageName = data.packageName)
}
