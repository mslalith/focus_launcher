package dev.mslalith.focuslauncher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.data.App
import dev.mslalith.focuslauncher.utils.Constants

@Entity(tableName = Constants.Database.APPS_TABLE_NAME)
data class AppRoom(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val isSystem: Boolean,
) {
    // TODO: @ms: refactor this to DTO mappings
    companion object {
        fun fromApp(app: App) = AppRoom(
            name = app.name,
            packageName = app.packageName,
            isSystem = app.isSystem
        )
    }

    fun toApp() = App(
        name = name,
        packageName = packageName,
        isSystem = isSystem
    )
}
