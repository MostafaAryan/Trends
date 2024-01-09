package com.programmerofpersia.trends.data.domain.repository.fake

import com.programmerofpersia.trends.data.domain.TrendsLocation
import com.programmerofpersia.trends.data.domain.model.DailyTrendsInfo
import com.programmerofpersia.trends.data.domain.model.TrendingSearchDayInfo
import com.programmerofpersia.trends.data.domain.model.TrendingSearchInfo
import com.programmerofpersia.trends.data.domain.repository.TrendingRepository
import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.model.TrResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*
* todo : apiExecutor instance is not needed here.
* */
class FakeTrendingRepositoryImpl(override val apiExecutor: ApiExecutor) : TrendingRepository {

    companion object {

        // todo : move all fake instances into another directory.
        val fakeDailyTrendsInfo = DailyTrendsInfo(
            "",
            listOf(
                TrendingSearchDayInfo(
                    "",
                    listOf(
                        TrendingSearchInfo(
                            "",
                            "",
                            listOf(),
                            null,
                            listOf(),
                            ""
                        )
                    )
                )
            )
        )

    }

    override suspend fun loadCookiesFromGoogleTrends() {
        TODO("Not yet implemented")
    }

    override fun loadDailyTrends(location: String): Flow<TrResponse<DailyTrendsInfo>> = flow {
        if (location == TrendsLocation.DEFAULT_LOCATION_ID) {
            emit(TrResponse.Success(fakeDailyTrendsInfo))
        } else {
            emit(TrResponse.Error("Location should not be empty!"))
        }
    }


}