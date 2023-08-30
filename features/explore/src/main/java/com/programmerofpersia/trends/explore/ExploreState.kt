package com.programmerofpersia.trends.explore

import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo

data class ExploreState(
    val geoList: GeoInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
