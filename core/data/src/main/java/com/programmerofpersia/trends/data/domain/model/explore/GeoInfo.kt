package com.programmerofpersia.trends.data.domain.model.explore

data class GeoInfo(
    val children: List<GeoInfo>,
    val name: String,
    val id: String,
)
