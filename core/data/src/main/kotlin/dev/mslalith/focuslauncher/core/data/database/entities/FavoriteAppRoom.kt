package dev.mslalith.focuslauncher.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.core.model.Constants.Database.FAVORITE_APPS_TABLE_NAME

@Entity(tableName = FAVORITE_APPS_TABLE_NAME)
internal data class FavoriteAppRoom(
    @PrimaryKey(autoGenerate = false)
    val packageName: String
)
