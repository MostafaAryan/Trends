package com.programmerofpersia.trends.data.remote.model.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ArticleDto(
    @JsonNames("articleTitle")
    val title: String,
    @JsonNames("time")
    val timeAgo: String,
    val image: ImageDto? = null,
    val url: String,
    val source: String? = null,
    val snippet: String,
)
