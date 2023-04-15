package dev.mslalith.focuslauncher.core.launcherapps.di.test

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.mslalith.focuslauncher.core.launcherapps.di.LauncherAppsModule
import dev.mslalith.focuslauncher.core.launcherapps.manager.icons.IconManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.icons.impl.IconManagerImpl
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.IconPackManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.iconpack.impl.IconPackManagerImpl
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.IconProvider
import dev.mslalith.focuslauncher.core.launcherapps.providers.icons.test.TestIconProvider
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LauncherAppsModule::class]
)
internal object TestLauncherAppsModule {

    @Provides
    @Singleton
    fun provideLauncherAppsManager(): LauncherAppsManager = TestLauncherAppsManager()

    @Provides
    @Singleton
    fun provideIconManager(@ApplicationContext context: Context): IconManager = IconManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideIconPackManager(@ApplicationContext context: Context): IconPackManager = IconPackManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideIconProvider(): IconProvider = TestIconProvider()
}
