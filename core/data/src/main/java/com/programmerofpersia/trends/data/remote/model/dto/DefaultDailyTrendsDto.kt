package com.programmerofpersia.trends.data.remote.model.dto

import kotlinx.serialization.Serializable


@Serializable
data class DefaultDailyTrendsDto(
    val trendingSearchesDays: List<TrendingSearchDayDto>,
    val endDateForNextRequest: String,
    val rssFeedPageUrl: String,
)
