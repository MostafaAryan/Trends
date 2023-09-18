package com.programmerofpersia.trends.data.remote

import com.programmerofpersia.trends.data.remote.model.dto.explore.CategoryDto
import com.programmerofpersia.trends.data.remote.model.dto.explore.GeoDto
import com.programmerofpersia.trends.data.remote.model.response.DailyTrendsResponse
import com.programmerofpersia.trends.data.remote.model.response.ExploreDetailsResponse
import com.programmerofpersia.trends.data.remote.model.response.SearchedKeywordsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface TrApi {

    @GET
    suspend fun callGoogleTrends(@Url url: String = Constants.BASE_TRENDS_URL): Response<Void>

    @GET("${Constants.DAILY_TRENDS}?hl=en-US&tz=-210&hl=en-US&ns=15")
    suspend fun fetchDailyTrends(
        @Query("geo") location: String
    ): Response<DailyTrendsResponse>

    @GET("${Constants.GEO_LIST}?hl=en-US&tz=-210")
    suspend fun fetchGeoList(): Response<GeoDto>

    @GET("${Constants.CATEGORY_LIST}?hl=en-US&tz=-210")
    suspend fun fetchCategoryList(): Response<CategoryDto>

    @GET("${Constants.EXPLORE_DETAILS}?hl=en-US&tz=-210")
    suspend fun fetchExploreDetails(
        @Query("req") requestDetails: String
    ): Response<ExploreDetailsResponse>

    @GET("${Constants.SEARCHED_KEYWORDS}?hl=en-US&tz=-210")
    suspend fun fetchSearchedKeywords(
        @Query("req") requestDetails: String,
        @Query("token") token: String
    ): Response<SearchedKeywordsResponse>
}