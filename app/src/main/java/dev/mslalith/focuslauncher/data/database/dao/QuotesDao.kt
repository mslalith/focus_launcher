package dev.mslalith.focuslauncher.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mslalith.focuslauncher.data.database.entities.Quote
import dev.mslalith.focuslauncher.utils.Constants.Database.QUOTES_TABLE_NAME

@Dao
interface QuotesDao {

    @Query("SELECT * FROM $QUOTES_TABLE_NAME")
    suspend fun getQuotes(): List<Quote>

    @Query("SELECT * FROM $QUOTES_TABLE_NAME WHERE id = :id LIMIT 1")
    suspend fun getQuoteBy(id: Int): Quote?

    @Query("DELETE FROM $QUOTES_TABLE_NAME")
    suspend fun clearQuotes()

    @Query("SELECT COUNT(*) FROM $QUOTES_TABLE_NAME")
    suspend fun getQuotesSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuotes(quotes: List<Quote>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuote(quote: Quote)

    @Delete
    suspend fun removeQuote(quote: Quote)
}
