package dev.mslalith.focuslauncher.data.api

import dev.mslalith.focuslauncher.data.models.QuotesApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesApi {
    @GET("/quotes")
    suspend fun getQuotes(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 150,
    ): QuotesApiResponse
}
