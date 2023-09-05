package com.programmerofpersia.trends.explore

import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordQueryInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordTopicInfo

data class ExploreState(
    val geoList: GeoInfo? = null,
    val categoryList: CategoryInfo? = null,
    val searchedTopicsList: List<KeywordTopicInfo>? = null,
    val searchedQueriesList: List<KeywordQueryInfo>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {

    fun areAllScreenDataAvailable() =
        geoList != null
                && categoryList != null
                && searchedTopicsList != null
                && searchedQueriesList != null

    /*var isLoading: Boolean = false
        set(value) {
            if (!value
                && ((geoList != null
                        && categoryList != null
                        && searchedTopicsList != null
                        && searchedQueriesList != null) || error != null)
            ) field = false
            field = value
        }*/
}

fun ExploreState.attemptHidingLoading(): ExploreState {
    return if (
        ((geoList != null
                && categoryList != null
                && searchedTopicsList != null
                && searchedQueriesList != null) || error != null)
    ) this.copy(
        isLoading = false
    ) else this
}
