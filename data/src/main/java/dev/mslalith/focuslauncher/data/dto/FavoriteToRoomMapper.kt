package dev.mslalith.focuslauncher.data.dto

import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.entities.FavoriteAppRoom
import dev.mslalith.focuslauncher.data.di.modules.AppToRoomMapperProvider
import dev.mslalith.focuslauncher.data.model.App
import javax.inject.Inject

class FavoriteToRoomMapper @Inject constructor(
    private val appsDao: AppsDao,
    @AppToRoomMapperProvider private val appToRoomMapper: AppToRoomMapper
) : Mapper<FavoriteAppRoom, App> {
    override fun fromEntity(data: FavoriteAppRoom): App {
        val app = appsDao.getAppBySync(data.packageName)
            ?: throw IllegalStateException("${data.packageName} app was not found in Database")
        return appToRoomMapper.fromEntity(app)
    }

    override fun toEntity(data: App) = FavoriteAppRoom(packageName = data.packageName)
}
