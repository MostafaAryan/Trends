package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.TrApi
import com.programmerofpersia.trends.data.remote.model.TrResponse
import com.programmerofpersia.trends.data.remote.model.mappers.toGeoInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ExploreRepositoryImpl(
    private val trApi: TrApi,
    override val apiExecutor: ApiExecutor
) : ExploreRepository {

    override fun loadGeoList(): Flow<TrResponse<GeoInfo>> = flow {
        emitApiResponse {
            trApi.fetchGeoList()
        }
    }.map { trResponse ->
        // map dto to domain-model (somethingInfo):
        when (trResponse) {
            is TrResponse.Success -> TrResponse.Success(trResponse.result?.toGeoInfo())
            is TrResponse.Error -> TrResponse.Error(trResponse.message)
        }
    }

}