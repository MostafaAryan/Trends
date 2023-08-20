package com.programmerofpersia.trends.data.domain.model


data class TrendingSearchDayInfo(
    val formattedDate: String,
    val trendingSearches: List<TrendingSearchInfo>,
)
