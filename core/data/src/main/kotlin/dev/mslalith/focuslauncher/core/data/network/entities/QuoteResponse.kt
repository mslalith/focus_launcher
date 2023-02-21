package dev.mslalith.focuslauncher.core.data.network.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class QuoteResponse(
    @SerialName(value = "_id")
    val id: String,
    @SerialName(value = "content")
    val quote: String,
    @SerialName(value = "author")
    val author: String,
    @SerialName(value = "authorSlug")
    val authorSlug: String,
    @SerialName(value = "length")
    val length: Int,
    @SerialName(value = "tags")
    val tags: List<String>
)

@Serializable
internal data class QuotesApiResponse(
    @SerialName(value = "count")
    val count: Int,
    @SerialName(value = "totalCount")
    val totalCount: Int,
    @SerialName(value = "page")
    val page: Int,
    @SerialName(value = "totalPages")
    val totalPages: Int,
    @SerialName(value = "lastItemIndex")
    val lastItemIndex: Int,
    @SerialName(value = "results")
    val results: List<QuoteResponse>
)
