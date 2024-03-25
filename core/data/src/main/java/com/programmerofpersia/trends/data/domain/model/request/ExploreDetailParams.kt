package com.programmerofpersia.trends.data.domain.model.request

import kotlinx.serialization.Serializable


@Serializable
data class ExploreDetailParams private constructor(
    val comparisonItem: List<GeoAndTime>,
    val category: Int,
    val property: String
) {

    @Serializable
    data class GeoAndTime(
        val keyword: String? = null,
        val geo: String,
        val time: String,
    )

    companion object {
        fun create(
            keyword: String?,
            geoId: String,
            dateId: String,
            categoryId: String,
            searchTypeId: String,
        ): ExploreDetailParams {

            val categoryIdAsInt = try {
                categoryId.toInt()
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                0
            }

            return ExploreDetailParams(
                listOf(GeoAndTime(keyword, geoId, dateId)),
                categoryIdAsInt,
                searchTypeId
            )

        }
    }


}
