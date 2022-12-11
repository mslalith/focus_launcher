package dev.mslalith.focuslauncher.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.data.utils.Constants

@Entity(tableName = Constants.Database.APPS_TABLE_NAME)
data class AppRoom(
    val name: String,
    @ColumnInfo(defaultValue = "'name'")
    val displayName: String,
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val isSystem: Boolean,
)
