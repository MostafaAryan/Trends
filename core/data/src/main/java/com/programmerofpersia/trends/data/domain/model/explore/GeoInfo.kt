package com.programmerofpersia.trends.data.domain.model.explore

data class GeoInfo(
    val children: List<GeoInfo>,
    override val name: String,
    override val id: String,
) : BaseFilterInfo
