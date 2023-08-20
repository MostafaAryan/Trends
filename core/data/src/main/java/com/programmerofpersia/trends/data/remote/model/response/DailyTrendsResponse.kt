package com.programmerofpersia.trends.data.remote.model.response

import com.programmerofpersia.trends.data.remote.model.dto.DefaultDailyTrendsDto
import kotlinx.serialization.Serializable


@Serializable
data class DailyTrendsResponse(
    val default: DefaultDailyTrendsDto,
)
