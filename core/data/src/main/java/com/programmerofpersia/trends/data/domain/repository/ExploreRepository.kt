package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.remote.model.TrResponse
import kotlinx.coroutines.flow.Flow

interface ExploreRepository : Repository {

    fun loadGeoList(): Flow<TrResponse<GeoInfo>>

}