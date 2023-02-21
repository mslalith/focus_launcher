package dev.mslalith.focuslauncher.core.data.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.serializers.CityJsonParser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SerializerModule {

    @Provides
    @Singleton
    fun provideCityJsonParser() = CityJsonParser()
}
