package com.programmerofpersia.trends.data.domain.model



data class DailyTrendsInfo(
    val rssFeedPageUrl: String,
    val trendingSearchesDays: List<TrendingSearchDayInfo>,
)
