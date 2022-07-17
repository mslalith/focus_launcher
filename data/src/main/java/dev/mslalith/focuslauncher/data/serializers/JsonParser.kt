package dev.mslalith.focuslauncher.data.serializers

import dev.mslalith.focuslauncher.data.model.places.City
import kotlinx.serialization.json.Json

interface JsonParser<T> {
    fun fromJson(json: String): T
    fun toJson(data: T): String
}

class CityJsonParser : JsonParser<City> {
    override fun fromJson(json: String): City = Json.decodeFromString(City.serializer(), json)
    override fun toJson(data: City): String = Json.encodeToString(City.serializer(), data)
}
