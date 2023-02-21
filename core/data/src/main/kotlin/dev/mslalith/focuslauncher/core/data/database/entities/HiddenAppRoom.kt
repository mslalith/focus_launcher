package dev.mslalith.focuslauncher.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.core.data.utils.Constants.Database.HIDDEN_APPS_TABLE_NAME

@Entity(tableName = HIDDEN_APPS_TABLE_NAME)
internal data class HiddenAppRoom(
    @PrimaryKey(autoGenerate = false)
    val packageName: String
)
