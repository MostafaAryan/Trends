package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.domain.model.DailyTrendsInfo
import com.programmerofpersia.trends.data.remote.model.TrResponse
import kotlinx.coroutines.flow.Flow

interface TrendingRepository : Repository {

    suspend fun loadCookiesFromGoogleTrends()

    fun loadDailyTrends(location: String): Flow<TrResponse<DailyTrendsInfo>>

}