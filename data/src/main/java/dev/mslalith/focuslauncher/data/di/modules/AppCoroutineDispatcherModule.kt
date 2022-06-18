package dev.mslalith.focuslauncher.data.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.data.utils.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.data.utils.AppCoroutineDispatcherImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppCoroutineDispatcherModule {

    @Provides
    @Singleton
    fun provideAppCoroutineDispatcher(): AppCoroutineDispatcher = AppCoroutineDispatcherImpl()
}
