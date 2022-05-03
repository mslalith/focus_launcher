package dev.mslalith.focuslauncher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.data.utils.Constants

@Entity(tableName = Constants.Database.QUOTES_TABLE_NAME)
data class QuoteRoom(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val quote: String,
    val author: String,
    val authorSlug: String,
    val length: Int,
    val tags: List<String>,
)
