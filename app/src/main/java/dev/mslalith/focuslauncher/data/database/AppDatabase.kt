package dev.mslalith.focuslauncher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.data.database.entities.App
import dev.mslalith.focuslauncher.data.database.entities.FavoriteAppRoom
import dev.mslalith.focuslauncher.data.database.entities.HiddenAppRoom
import dev.mslalith.focuslauncher.data.database.entities.Quote

@Database(
    entities = [
        App::class,
        FavoriteAppRoom::class,
        HiddenAppRoom::class,
        Quote::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appsDao(): AppsDao
    abstract fun favoriteAppsDao(): FavoriteAppsDao
    abstract fun hiddenAppsDao(): HiddenAppsDao
    abstract fun quotesDao(): QuotesDao
}
