package com.programmerofpersia.trends.data.remote.model.response

import com.programmerofpersia.trends.data.remote.model.dto.explore.details.WidgetDto
import kotlinx.serialization.Serializable

@Serializable
data class ExploreDetailsResponse(
    val widgets: List<WidgetDto>
)
