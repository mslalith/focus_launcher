package dev.mslalith.focuslauncher.core.data.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.core.data.network.api.impl.PlacesApiImpl
import dev.mslalith.focuslauncher.core.data.network.api.impl.QuotesApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient = HttpClient(engineFactory = Android) {
        install(plugin = ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }

        val timeout = 15_000L
        install(plugin = HttpTimeout) {
            requestTimeoutMillis = timeout
            connectTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }

    @Provides
    @Singleton
    fun provideQuoteApi(httpClient: HttpClient): QuotesApi = QuotesApiImpl(httpClient = httpClient)

    @Provides
    @Singleton
    fun providePlacesApi(httpClient: HttpClient): PlacesApi = PlacesApiImpl(httpClient = httpClient)
}
