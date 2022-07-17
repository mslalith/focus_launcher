package dev.mslalith.focuslauncher.data.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.data.utils.UpdateManager
import dev.mslalith.focuslauncher.data.utils.location.LocationManager
import dev.mslalith.focuslauncher.data.utils.location.LocationManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Provides
    @Singleton
    fun provideUpdateManager(@ApplicationContext context: Context) = UpdateManager(context)

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager = LocationManagerImpl(context)
}
