package dev.mslalith.focuslauncher.core.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.impl.AppCoroutineDispatcherImpl
import dev.mslalith.focuslauncher.core.common.network.impl.ConnectivityManagerNetworkMonitorImpl
import dev.mslalith.focuslauncher.core.common.network.NetworkMonitor
import dev.mslalith.focuslauncher.core.common.providers.clock.ClockProvider
import dev.mslalith.focuslauncher.core.common.providers.clock.impl.ClockProviderImpl
import dev.mslalith.focuslauncher.core.common.providers.randomnumber.RandomNumberProvider
import dev.mslalith.focuslauncher.core.common.providers.randomnumber.impl.RandomNumberProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Provides
    @Singleton
    fun provideAppCoroutineDispatcher(): AppCoroutineDispatcher = AppCoroutineDispatcherImpl()

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = ConnectivityManagerNetworkMonitorImpl(context = context)

    @Provides
    @Singleton
    fun provideRandomNumber(): RandomNumberProvider = RandomNumberProviderImpl()

    @Provides
    @Singleton
    fun provideClockProvider(): ClockProvider = ClockProviderImpl()
}
