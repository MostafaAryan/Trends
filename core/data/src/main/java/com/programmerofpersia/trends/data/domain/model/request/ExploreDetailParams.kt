package com.programmerofpersia.trends.data.domain.model.request

import kotlinx.serialization.Serializable


@Serializable
data class ExploreDetailParams private constructor(
    val comparisonItem: List<GeoAndTime>,
    val category: String,
    val property: String
) {

    @Serializable
    data class GeoAndTime(
        val geo: String,
        val time: String,
    )

    companion object {
        fun create(
            geoId: String,
            dateId: String,
            categoryId: String,
            searchTypeId: String,
        ) = ExploreDetailParams(listOf(GeoAndTime(geoId, dateId)), categoryId, searchTypeId)
    }


}
