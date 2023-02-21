package dev.mslalith.focuslauncher.core.data.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.utils.Constants.DataStore.SETTINGS_DATASTORE
import dev.mslalith.focuslauncher.core.data.utils.Constants.DataStore.THEME_DATASTORE
import javax.inject.Qualifier
import javax.inject.Singleton

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = THEME_DATASTORE)
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATASTORE)

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @ThemeProvider
    @Provides
    @Singleton
    fun provideThemeDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.themeDataStore

    @SettingsProvider
    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.settingsDataStore
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class ThemeProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class SettingsProvider
