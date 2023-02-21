package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.database.entities.FavoriteAppRoom
import dev.mslalith.focuslauncher.core.data.di.modules.AppToRoomMapperProvider
import dev.mslalith.focuslauncher.core.model.App
import javax.inject.Inject

internal class FavoriteToRoomMapper @Inject constructor(
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
