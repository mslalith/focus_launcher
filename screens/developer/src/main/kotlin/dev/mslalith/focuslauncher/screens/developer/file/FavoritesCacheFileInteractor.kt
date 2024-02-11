package dev.mslalith.focuslauncher.screens.developer.file

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mslalith.focuslauncher.core.model.app.App
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FavoritesCacheFileInteractor @Inject constructor(
    @ApplicationContext private val context: Context
) : CacheFileInteractor<List<App>>(
    context = context
) {

    override val fileName: String = "saved_favorites.json"

    override fun default(): List<App> = emptyList()

    override fun List<App>.toJson(): String = Json.encodeToString(
        serializer = ListSerializer(elementSerializer = App.serializer()),
        value = this
    )

    override fun fromJson(json: String): List<App> = Json.decodeFromString(string = json)
}
