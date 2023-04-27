package dev.mslalith.focuslauncher.core.data.di.modules.test

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.mslalith.focuslauncher.core.data.di.modules.NetworkModule
import dev.mslalith.focuslauncher.core.data.network.api.PlacesApi
import dev.mslalith.focuslauncher.core.data.network.api.QuotesApi
import dev.mslalith.focuslauncher.core.data.network.api.fakes.FakePlacesApi
import dev.mslalith.focuslauncher.core.data.network.api.fakes.FakeQuotesApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
internal object TestNetworkModule {

    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient = HttpClient(engineFactory = Android) {
        install(plugin = ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
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
    fun provideQuoteApi(): QuotesApi = FakeQuotesApi()

    @Provides
    @Singleton
    fun providePlacesApi(): PlacesApi = FakePlacesApi()
}
