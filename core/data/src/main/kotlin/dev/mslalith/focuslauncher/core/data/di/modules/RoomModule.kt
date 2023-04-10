package dev.mslalith.focuslauncher.core.data.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.database.AppDatabase
import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.PlacesDao
import dev.mslalith.focuslauncher.core.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.core.data.database.utils.CloseDatabase
import dev.mslalith.focuslauncher.core.data.utils.Constants.Database.APP_DB_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        APP_DB_NAME
    ).build()

    @Provides
    @Singleton
    fun provideCloseDatabase(appDatabase: AppDatabase): CloseDatabase = { appDatabase.close() }

    @Provides
    @Singleton
    fun provideAppsDao(appDatabase: AppDatabase): AppsDao = appDatabase.appsDao()

    @Provides
    @Singleton
    fun provideFavoriteAppsDao(appDatabase: AppDatabase): FavoriteAppsDao = appDatabase.favoriteAppsDao()

    @Provides
    @Singleton
    fun provideHiddenAppsDao(appDatabase: AppDatabase): HiddenAppsDao = appDatabase.hiddenAppsDao()

    @Provides
    @Singleton
    fun provideQuotesDao(appDatabase: AppDatabase): QuotesDao = appDatabase.quotesDao()

    @Provides
    @Singleton
    fun providePlacesDao(appDatabase: AppDatabase): PlacesDao = appDatabase.placesDao()
}
