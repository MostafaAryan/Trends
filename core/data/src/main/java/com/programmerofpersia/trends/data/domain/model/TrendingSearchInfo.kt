package com.programmerofpersia.trends.data.domain.model


data class TrendingSearchInfo(
    val title: String,
    val formattedTraffic: String,
    val relatedQueries: List<QueryInfo>,
    val image: ImageInfo? = null,
    val articles: List<ArticleInfo>,
    val shareUrl: String,
)
