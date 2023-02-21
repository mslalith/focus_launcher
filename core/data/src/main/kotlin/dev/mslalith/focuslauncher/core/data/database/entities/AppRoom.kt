package dev.mslalith.focuslauncher.core.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.core.data.utils.Constants.Database.APPS_TABLE_NAME

@Entity(tableName = APPS_TABLE_NAME)
internal data class AppRoom(
    val name: String,
    @ColumnInfo(defaultValue = "'name'")
    val displayName: String,
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val isSystem: Boolean
)
