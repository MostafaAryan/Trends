package com.programmerofpersia.trends.data.remote.model.mappers

import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.remote.model.dto.explore.GeoDto


fun GeoDto.toGeoInfo(): GeoInfo = GeoInfo(
    childrenList.map { it.toGeoInfo() },
    name,
    id
)