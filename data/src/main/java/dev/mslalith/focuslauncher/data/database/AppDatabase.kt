package dev.mslalith.focuslauncher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.data.database.entities.FavoriteAppRoom
import dev.mslalith.focuslauncher.data.database.entities.HiddenAppRoom
import dev.mslalith.focuslauncher.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.data.utils.Converters

@Database(
    entities = [
        AppRoom::class,
        FavoriteAppRoom::class,
        HiddenAppRoom::class,
        QuoteRoom::class
    ],
    version = 1,
    exportSchema = false
)
// TODO: @ms: are these Converters really necessary
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appsDao(): AppsDao
    abstract fun favoriteAppsDao(): FavoriteAppsDao
    abstract fun hiddenAppsDao(): HiddenAppsDao
    abstract fun quotesDao(): QuotesDao
}
