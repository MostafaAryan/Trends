package com.programmerofpersia.trends.data.remote.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrendingSearchDto(
    val title: TrendingSearchTitleDto,
    val formattedTraffic: String,
    val relatedQueries: List<RelatedQueriesDto>,
    val image: ImageDto,
    val articles: List<ArticleDto>,
    val shareUrl: String,
) {

    @Serializable
    data class TrendingSearchTitleDto(
        val query: String,
        val exploreLink: String,
    )

    @Serializable
    data class RelatedQueriesDto(
        val query: String,
        val exploreLink: String,
    )

}
