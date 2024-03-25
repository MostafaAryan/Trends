package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoAndCategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordInfo
import com.programmerofpersia.trends.data.domain.model.request.ExploreDetailParams
import com.programmerofpersia.trends.data.remote.model.TrResponse
import kotlinx.coroutines.flow.Flow

interface ExploreRepository : Repository {

    fun loadGeoList(): Flow<TrResponse<GeoInfo>>

    fun loadCategoryList(): Flow<TrResponse<CategoryInfo>>

    fun loadGeoAndCategoryLists(): Flow<TrResponse<GeoAndCategoryInfo>>

    fun loadRelatedSearches(
        searchedTopicsToken: String,
        searchedTopicsRequest: String,
        searchedQueriesToken: String,
        searchedQueriesRequest: String
    ): Flow<TrResponse<KeywordInfo>>

    fun loadSearches(queryParams: ExploreDetailParams): Flow<TrResponse<KeywordInfo>>

}