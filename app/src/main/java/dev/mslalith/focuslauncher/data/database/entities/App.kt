package dev.mslalith.focuslauncher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.utils.Constants

@Entity(tableName = Constants.Database.APPS_TABLE_NAME)
data class App(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val isSystem: Boolean,
)
