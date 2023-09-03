package com.programmerofpersia.trends.data.remote.model.dto.explore

import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    val mid: String,
    val title: String,
    val type: String
)
