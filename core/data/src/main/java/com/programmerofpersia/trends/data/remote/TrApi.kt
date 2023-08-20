package com.programmerofpersia.trends.data.remote

import com.programmerofpersia.trends.data.remote.model.response.DailyTrendsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TrApi {


    @GET("${Constants.DAILY_TRENDS}?hl=en-US&tz=-210&hl=en-US&ns=15")
    suspend fun fetchDailyTrends(
        @Query("geo") location: String
    ): Response<DailyTrendsResponse>


}