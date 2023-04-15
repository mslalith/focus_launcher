package dev.mslalith.focuslauncher.core.launcherapps.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.launcherapps.manager.icons.IconManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.icons.impl.IconManagerImpl
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.iconpack.impl.IconPackManagerImpl
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
    fun provideIconManager(@ApplicationContext context: Context): IconManager = IconManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideIconPackManager(@ApplicationContext context: Context): IconPackManager = IconPackManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideIconProvider(iconManager: IconManager): IconProvider = IconProviderImpl(iconManager = iconManager)
}
