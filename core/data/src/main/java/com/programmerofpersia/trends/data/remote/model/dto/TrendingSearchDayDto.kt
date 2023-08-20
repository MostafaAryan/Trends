package com.programmerofpersia.trends.data.remote.model.dto

import kotlinx.serialization.Serializable


@Serializable
data class TrendingSearchDayDto(
    val date : String,
    val formattedDate : String,
    val trendingSearches : List<TrendingSearchDto>,
)
