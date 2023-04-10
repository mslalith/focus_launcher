package dev.mslalith.focuslauncher.core.data.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.FavoriteToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.HiddenToRoomMapper
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DtoMapperModule {

    @AppToRoomMapperProvider
    @Provides
    @Singleton
    fun provideAppToRoomMapper(): AppToRoomMapper = AppToRoomMapper()

    @FavoriteToRoomMapperProvider
    @Provides
    @Singleton
    fun provideFavoriteToRoomMapper(
        appsDao: AppsDao,
        @AppToRoomMapperProvider appToRoomMapper: AppToRoomMapper
    ): FavoriteToRoomMapper = FavoriteToRoomMapper(
        appsDao = appsDao,
        appToRoomMapper = appToRoomMapper
    )

    @HiddenToRoomMapperProvider
    @Provides
    @Singleton
    fun provideHiddenToRoomMapper(
        appsDao: AppsDao,
        @AppToRoomMapperProvider appToRoomMapper: AppToRoomMapper
    ): HiddenToRoomMapper = HiddenToRoomMapper(
        appsDao = appsDao,
        appToRoomMapper = appToRoomMapper
    )
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class AppToRoomMapperProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class FavoriteToRoomMapperProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class HiddenToRoomMapperProvider
