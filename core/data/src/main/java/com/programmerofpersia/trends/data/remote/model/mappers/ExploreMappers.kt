package com.programmerofpersia.trends.data.remote.model.mappers

import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.remote.model.dto.explore.CategoryDto
import com.programmerofpersia.trends.data.remote.model.dto.explore.GeoDto


fun GeoDto.toGeoInfo(): GeoInfo = GeoInfo(
    childrenList.map { it.toGeoInfo() },
    name,
    id
)

fun CategoryDto.toCategoryInfo(): CategoryInfo = CategoryInfo(
    childrenList.map { it.toCategoryInfo() },
    name,
    id
)