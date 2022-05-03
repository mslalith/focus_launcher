package dev.mslalith.focuslauncher.data.di.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.data.repository.ClockRepo
import dev.mslalith.focuslauncher.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.data.repository.settings.QuotesSettingsRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Screen Data Repository providers
     */
    @Provides
    @Singleton
    fun provideAppDrawerRepo(appsDao: AppsDao) = AppDrawerRepo(appsDao)

    @Provides
    @Singleton
    fun provideFavoritesRepo(appsDao: AppsDao, favoriteAppsDao: FavoriteAppsDao) = FavoritesRepo(appsDao, favoriteAppsDao)

    @Provides
    @Singleton
    fun provideHiddenAppsRepo(appsDao: AppsDao, hiddenAppsDao: HiddenAppsDao) = HiddenAppsRepo(appsDao, hiddenAppsDao)

    @Provides
    @Singleton
    fun provideClockRepo(): ClockRepo = ClockRepo()

    @Provides
    @Singleton
    fun provideLunarPhaseDetailsRepo(clockRepo: ClockRepo): LunarPhaseDetailsRepo = LunarPhaseDetailsRepo(clockRepo)

    /**
     * API Repository providers
     */
    @Provides
    @Singleton
    fun provideQuotesRepo(quotesApi: QuotesApi, quotesDao: QuotesDao): QuotesRepo = QuotesRepo(quotesApi, quotesDao)

    /**
     * Settings Repository providers
     */
    @Provides
    @Singleton
    fun provideGeneralSettingsRepo(@SettingsProvider settingsDataStore: DataStore<Preferences>) = GeneralSettingsRepo(settingsDataStore)

    @Provides
    @Singleton
    fun provideAppDrawerSettingsRepo(@SettingsProvider settingsDataStore: DataStore<Preferences>) = AppDrawerSettingsRepo(settingsDataStore)

    @Provides
    @Singleton
    fun provideClockSettingsRepo(@SettingsProvider settingsDataStore: DataStore<Preferences>) = ClockSettingsRepo(settingsDataStore)

    @Provides
    @Singleton
    fun provideLunarPhaseSettingsRepo(@SettingsProvider settingsDataStore: DataStore<Preferences>) = LunarPhaseSettingsRepo(settingsDataStore)

    @Provides
    @Singleton
    fun provideQuotesSettingsRepo(@SettingsProvider settingsDataStore: DataStore<Preferences>) = QuotesSettingsRepo(settingsDataStore)
}
