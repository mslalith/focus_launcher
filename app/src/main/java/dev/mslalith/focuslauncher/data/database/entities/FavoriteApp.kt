package dev.mslalith.focuslauncher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.utils.Constants

@Entity(tableName = Constants.Database.FAVORITE_APPS_TABLE_NAME)
data class FavoriteApp(
    @PrimaryKey(autoGenerate = false)
    val packageName: String
)
