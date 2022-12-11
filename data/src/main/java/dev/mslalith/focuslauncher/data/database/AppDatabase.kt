package dev.mslalith.focuslauncher.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.mslalith.focuslauncher.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.data.database.dao.CitiesDao
import dev.mslalith.focuslauncher.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.data.database.entities.CityRoom
import dev.mslalith.focuslauncher.data.database.entities.FavoriteAppRoom
import dev.mslalith.focuslauncher.data.database.entities.HiddenAppRoom
import dev.mslalith.focuslauncher.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.data.database.typeconverter.Converters

@Database(
    entities = [
        AppRoom::class,
        FavoriteAppRoom::class,
        HiddenAppRoom::class,
        QuoteRoom::class,
        CityRoom::class
    ],
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appsDao(): AppsDao
    abstract fun favoriteAppsDao(): FavoriteAppsDao
    abstract fun hiddenAppsDao(): HiddenAppsDao
    abstract fun quotesDao(): QuotesDao
    abstract fun citiesDao(): CitiesDao
}
