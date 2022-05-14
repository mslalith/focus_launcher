package dev.mslalith.focuslauncher.data.database.typeconverter

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun jsonToStringList(json: String): List<String> {
        return Json.decodeFromString(ListSerializer(String.serializer()), json)
    }

    @TypeConverter
    fun stringListToJson(list: List<String>): String {
        return Json.encodeToString(ListSerializer(String.serializer()), list)
    }
}
