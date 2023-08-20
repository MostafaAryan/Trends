package com.programmerofpersia.trends.trending

import com.programmerofpersia.trends.data.domain.model.DailyTrendsInfo

data class TrendingState(
    val dailyTrendsInfo: DailyTrendsInfo? = null,
    val isLoading : Boolean = false,
    val error : String? = null,
)
