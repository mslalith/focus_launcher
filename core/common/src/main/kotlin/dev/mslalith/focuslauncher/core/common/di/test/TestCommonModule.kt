package dev.mslalith.focuslauncher.core.common.di.test

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.impl.AppCoroutineDispatcherImpl
import dev.mslalith.focuslauncher.core.common.di.CommonModule
import dev.mslalith.focuslauncher.core.common.network.ConnectivityManagerNetworkMonitor
import dev.mslalith.focuslauncher.core.common.network.NetworkMonitor
import dev.mslalith.focuslauncher.core.common.random.RandomNumber
import dev.mslalith.focuslauncher.core.common.random.test.TestRandomNumber
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CommonModule::class]
)
internal object TestCommonModule {

    @Provides
    @Singleton
    fun provideAppCoroutineDispatcher(): AppCoroutineDispatcher = AppCoroutineDispatcherImpl()

    @Provides
    @Singleton
    fun provideRandomNumber(): RandomNumber = TestRandomNumber()

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = ConnectivityManagerNetworkMonitor(context)
}
