package dev.mslalith.focuslauncher.di.modules

import android.content.Context
import dev.mslalith.focuslauncher.data.managers.UpdateManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Provides
    @Singleton
    fun provideUpdateManager(@ApplicationContext context: Context) = UpdateManager(context)
}
