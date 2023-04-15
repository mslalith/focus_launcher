package dev.mslalith.focuslauncher.core.launcherapps.di.test

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.mslalith.focuslauncher.core.launcherapps.di.LauncherAppsModule
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.LauncherAppsManager
import dev.mslalith.focuslauncher.core.launcherapps.manager.launcherapps.test.TestLauncherAppsManager
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
}
