package dev.mslalith.focuslauncher.core.data.serializers

import dev.mslalith.focuslauncher.core.model.City
import kotlinx.serialization.json.Json

internal interface JsonParser<T> {
    fun fromJson(json: String): T
    fun toJson(data: T): String
}

internal class CityJsonParser : JsonParser<City> {
    override fun fromJson(json: String): City = Json.decodeFromString(City.serializer(), json)
    override fun toJson(data: City): String = Json.encodeToString(City.serializer(), data)
}
