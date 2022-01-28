package dev.mslalith.focuslauncher.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson by lazy { Gson() }

    @TypeConverter
    fun jsonToStringList(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun stringListToJson(list: List<String>): String {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(list, type)
    }
}
