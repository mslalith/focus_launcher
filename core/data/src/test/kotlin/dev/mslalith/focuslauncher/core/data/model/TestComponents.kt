package dev.mslalith.focuslauncher.core.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.mslalith.focuslauncher.core.data.database.AppDatabase
import dev.mslalith.focuslauncher.core.data.dto.AppToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.FavoriteToRoomMapper
import dev.mslalith.focuslauncher.core.data.dto.HiddenToRoomMapper
import dev.mslalith.focuslauncher.core.data.serializers.CityJsonParser

internal data class TestComponents(
    val database: AppDatabase,
    val mappers: MapperComponents,
    val dataStore: DataStore<Preferences>,
    val cityJsonParser: CityJsonParser
)

internal data class MapperComponents(
    val appToRoomMapper: AppToRoomMapper,
    val favoriteToRoomMapper: FavoriteToRoomMapper,
    val hiddenToRoomMapper: HiddenToRoomMapper
)
