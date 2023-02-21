package dev.mslalith.focuslauncher.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mslalith.focuslauncher.core.data.utils.Constants.Database.QUOTES_TABLE_NAME

@Entity(tableName = QUOTES_TABLE_NAME)
internal data class QuoteRoom(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val quote: String,
    val author: String,
    val authorSlug: String,
    val length: Int,
    val tags: List<String>
)
