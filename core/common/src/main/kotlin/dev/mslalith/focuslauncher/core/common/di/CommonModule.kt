package dev.mslalith.focuslauncher.core.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.impl.AppCoroutineDispatcherImpl
import dev.mslalith.focuslauncher.core.common.network.ConnectivityManagerNetworkMonitor
import dev.mslalith.focuslauncher.core.common.network.NetworkMonitor
import dev.mslalith.focuslauncher.core.common.random.RandomNumber
import dev.mslalith.focuslauncher.core.common.random.RandomNumberImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideAppCoroutineDispatcher(): AppCoroutineDispatcher = AppCoroutineDispatcherImpl()

    @Provides
    @Singleton
    fun provideRandomNumber(): RandomNumber = RandomNumberImpl()

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = ConnectivityManagerNetworkMonitor(context)
}
