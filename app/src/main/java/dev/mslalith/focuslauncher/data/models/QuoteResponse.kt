package dev.mslalith.focuslauncher.data.models

import com.google.gson.annotations.SerializedName
import dev.mslalith.focuslauncher.data.database.entities.Quote

data class QuoteResponse(
    @SerializedName(value = "_id")
    val id: String,
    @SerializedName(value = "content")
    val quote: String,
    @SerializedName(value = "author")
    val author: String,
    @SerializedName(value = "authorSlug")
    val authorSlug: String,
    @SerializedName(value = "length")
    val length: Int,
    @SerializedName(value = "tags")
    val tags: List<String>,
) {
    fun toQuote() = Quote(
        id = id,
        quote = quote,
        author = author,
        authorSlug = authorSlug,
        length = length,
        tags = tags
    )
}

data class QuotesApiResponse(
    @SerializedName(value = "count")
    val count: Int,
    @SerializedName(value = "totalCount")
    val totalCount: Int,
    @SerializedName(value = "page")
    val page: Int,
    @SerializedName(value = "totalPages")
    val totalPages: Int,
    @SerializedName(value = "lastItemIndex")
    val lastItemIndex: Int,
    @SerializedName(value = "results")
    val results: List<QuoteResponse>,
)
