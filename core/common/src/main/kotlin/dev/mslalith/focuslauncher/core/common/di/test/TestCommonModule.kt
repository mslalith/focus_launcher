package dev.mslalith.focuslauncher.core.common.di.test

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.test.TestAppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.di.CommonModule
import dev.mslalith.focuslauncher.core.common.network.NetworkMonitor
import dev.mslalith.focuslauncher.core.common.network.test.FakeNetworkMonitor
import dev.mslalith.focuslauncher.core.common.providers.clock.ClockProvider
import dev.mslalith.focuslauncher.core.common.providers.clock.test.TestClockProvider
import dev.mslalith.focuslauncher.core.common.providers.randomnumber.RandomNumberProvider
import dev.mslalith.focuslauncher.core.common.providers.randomnumber.test.TestRandomNumberProvider
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CommonModule::class]
)
object TestCommonModule {

    @Provides
    @Singleton
    fun provideAppCoroutineDispatcher(): AppCoroutineDispatcher = TestAppCoroutineDispatcher(
        coroutineContext = Dispatchers.Main as CoroutineContext
    )

    @Provides
    @Singleton
    fun provideNetworkMonitor(): NetworkMonitor = FakeNetworkMonitor()

    @Provides
    @Singleton
    fun provideRandomNumberProvider(testRandomNumber: TestRandomNumberProvider): RandomNumberProvider = testRandomNumber

    @Provides
    @Singleton
    fun provideTestRandomNumberProvider(): TestRandomNumberProvider = TestRandomNumberProvider()

    @Provides
    @Singleton
    fun provideClockProvider(testClockProvider: TestClockProvider): ClockProvider = testClockProvider

    @Provides
    @Singleton
    fun provideTestClockProvider(): TestClockProvider = TestClockProvider()
}
