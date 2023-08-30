package com.programmerofpersia.trends.data.remote.model.dto.explore

import kotlinx.serialization.Serializable

@Serializable
data class GeoDto(
    private val children: List<GeoDto>? = null,
    val name: String,
    val id: String,
) {

    val childrenList: List<GeoDto>
        get() = children ?: listOf()

}