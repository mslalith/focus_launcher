package dev.mslalith.focuslauncher.core.launcherapps.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.impl.LauncherAppsManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LauncherAppsModule {

    @Provides
    @Singleton
    fun provideLauncherAppsManager(@ApplicationContext context: Context): LauncherAppsManager = LauncherAppsManagerImpl(context)
}
