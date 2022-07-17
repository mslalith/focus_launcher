package dev.mslalith.focuslauncher.data.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.data.serializers.CityJsonParser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializerModule {

    @Provides
    @Singleton
    fun provideCityJsonParser() = CityJsonParser()
}
