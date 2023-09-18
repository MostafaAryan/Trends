package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordInfo
import com.programmerofpersia.trends.data.domain.model.request.ExploreDetailParams
import com.programmerofpersia.trends.data.remote.model.TrResponse
import kotlinx.coroutines.flow.Flow

interface ExploreRepository : Repository {

    fun loadGeoList(): Flow<TrResponse<GeoInfo>>

    fun loadCategoryList(): Flow<TrResponse<CategoryInfo>>

    fun loadSearches(queryParams: ExploreDetailParams): Flow<TrResponse<KeywordInfo>>

}