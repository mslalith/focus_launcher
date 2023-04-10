package dev.mslalith.focuslauncher.core.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import dev.mslalith.focuslauncher.core.data.database.dao.AppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.FavoriteAppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.HiddenAppsDao
import dev.mslalith.focuslauncher.core.data.database.dao.PlacesDao
import dev.mslalith.focuslauncher.core.data.database.dao.QuotesDao
import dev.mslalith.focuslauncher.core.data.database.entities.AppRoom
import dev.mslalith.focuslauncher.core.data.database.entities.FavoriteAppRoom
import dev.mslalith.focuslauncher.core.data.database.entities.HiddenAppRoom
import dev.mslalith.focuslauncher.core.data.database.entities.PlaceRoom
import dev.mslalith.focuslauncher.core.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.core.data.database.typeconverter.Converters

@Database(
    entities = [
        AppRoom::class,
        FavoriteAppRoom::class,
        HiddenAppRoom::class,
        QuoteRoom::class,
        PlaceRoom::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = MigrateFrom2To3::class),
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun appsDao(): AppsDao
    abstract fun favoriteAppsDao(): FavoriteAppsDao
    abstract fun hiddenAppsDao(): HiddenAppsDao
    abstract fun quotesDao(): QuotesDao
    abstract fun placesDao(): PlacesDao
}

@DeleteTable(tableName = "cities")
private class MigrateFrom2To3 : AutoMigrationSpec
