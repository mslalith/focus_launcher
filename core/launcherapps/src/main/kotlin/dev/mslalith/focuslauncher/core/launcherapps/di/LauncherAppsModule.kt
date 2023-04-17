package dev.mslalith.focuslauncher.core.launcherapps.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache.IconCacheManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconcache.impl.IconCacheManagerImpl
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.impl.IconPackManagerImpl
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.impl.LauncherAppsManagerImpl
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.impl.IconProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LauncherAppsModule {

    @Provides
    @Singleton
    fun provideLauncherAppsManager(@ApplicationContext context: Context): LauncherAppsManager = LauncherAppsManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideIconCacheManager(@ApplicationContext context: Context): IconCacheManager = IconCacheManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideIconPackManager(
        @ApplicationContext context: Context,
        iconCacheManager: IconCacheManager
    ): IconPackManager = IconPackManagerImpl(context = context, iconCacheManager = iconCacheManager)

    @Provides
    @Singleton
    fun provideIconProvider(iconCacheManager: IconCacheManager): IconProvider = IconProviderImpl(iconCacheManager = iconCacheManager)
}
