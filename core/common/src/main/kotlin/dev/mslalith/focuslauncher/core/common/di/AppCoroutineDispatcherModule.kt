package dev.mslalith.focuslauncher.core.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.common.AppCoroutineDispatcherImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppCoroutineDispatcherModule {

    @Provides
    @Singleton
    fun provideAppCoroutineDispatcher(): AppCoroutineDispatcher = AppCoroutineDispatcherImpl()
}
