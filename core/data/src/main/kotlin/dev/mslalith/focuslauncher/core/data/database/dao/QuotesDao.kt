package dev.mslalith.focuslauncher.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.core.data.database.entities.QuoteRoom
import dev.mslalith.focuslauncher.core.model.Constants.Database.QUOTES_TABLE_NAME

@Dao
internal interface QuotesDao {

    @Query("SELECT * FROM $QUOTES_TABLE_NAME")
    suspend fun getQuotes(): List<QuoteRoom>

    @Query("SELECT * FROM $QUOTES_TABLE_NAME WHERE id = :id LIMIT 1")
    suspend fun getQuoteBy(id: String): QuoteRoom?

    @Query("SELECT * FROM $QUOTES_TABLE_NAME WHERE id = :id LIMIT 1")
    fun getQuoteBySync(id: String): QuoteRoom?

    @Query("DELETE FROM $QUOTES_TABLE_NAME")
    suspend fun clearQuotes()

    @Query("SELECT COUNT(*) FROM $QUOTES_TABLE_NAME")
    suspend fun getQuotesSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuotes(quotes: List<QuoteRoom>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuote(quote: QuoteRoom)

    @Delete
    suspend fun removeQuote(quote: QuoteRoom)
}
