package dev.mslalith.focuslauncher.di.modules

import dev.mslalith.focuslauncher.data.api.QuotesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val QUOTES_BASE_URL = "https://api.quotable.io"

    @Provides
    @Singleton
    fun provideQuoteApi(): QuotesApi = Retrofit.Builder().baseUrl(QUOTES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuotesApi::class.java)
}
