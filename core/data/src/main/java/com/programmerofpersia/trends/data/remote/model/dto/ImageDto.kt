package com.programmerofpersia.trends.data.remote.model.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ImageDto(
    val newsUrl: String,
    val source: String,
    @JsonNames("imgUrl")
    val imageUrl: String,
) {
}
