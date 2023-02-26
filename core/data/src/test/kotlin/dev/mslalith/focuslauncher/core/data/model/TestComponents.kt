package dev.mslalith.focuslauncher.core.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.mslalith.focuslauncher.core.common.appcoroutinedispatcher.AppCoroutineDispatcher
import dev.mslalith.focuslauncher.core.data.database.AppDatabase
import dev.mslalith.focuslauncher.core.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.FavoriteToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.HiddenToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.QuoteResponseToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.QuoteToRoomMapper
import dev.mslalith.focuslauncher.core.data.network.api.fakes.FakePlacesApi
import dev.mslalith.focuslauncher.core.data.network.api.fakes.FakeQuotesApi
import dev.mslalith.focuslauncher.core.data.serializers.CityJsonParser

internal data class TestComponents(
    val database: AppDatabase,
    val apis: ApiComponents,
    val mappers: MapperComponents,
    val dataStore: DataStore<Preferences>,
    val cityJsonParser: CityJsonParser,
    val appCoroutineDispatcher: AppCoroutineDispatcher
)

internal data class ApiComponents(
    val placesApi: FakePlacesApi,
    val quotesApi: FakeQuotesApi
)

internal data class MapperComponents(
    val appToRoomMapper: AppToRoomMapper,
    val favoriteToRoomMapper: FavoriteToRoomMapper,
    val hiddenToRoomMapper: HiddenToRoomMapper,
    val quoteResponseToRoomMapper: QuoteResponseToRoomMapper,
    val quoteToRoomMapper: QuoteToRoomMapper
)
