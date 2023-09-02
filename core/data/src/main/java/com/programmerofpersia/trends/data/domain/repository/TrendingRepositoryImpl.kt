package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.domain.model.DailyTrendsInfo
import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.TrApi
import com.programmerofpersia.trends.data.remote.model.TrResponse
import com.programmerofpersia.trends.data.remote.model.mappers.toDailyTrendsInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TrendingRepositoryImpl(
    private val trApi: TrApi,
    override val apiExecutor: ApiExecutor
) : TrendingRepository {

    override suspend fun loadCookiesFromGoogleTrends() {
        trApi.callGoogleTrends()
    }

    override fun loadDailyTrends(location: String): Flow<TrResponse<DailyTrendsInfo>> = flow {
        emitApiResponse {
            trApi.fetchDailyTrends(location)
        }
    }.map { trResponse ->
        // map dto to domain-model (somethingInfo):
        when (trResponse) {
            is TrResponse.Success -> TrResponse.Success(trResponse.result?.default?.toDailyTrendsInfo())
            is TrResponse.Error -> TrResponse.Error(trResponse.message)
        }
    }


}