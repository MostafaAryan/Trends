package com.programmerofpersia.trends.data.domain.model

data class ArticleInfo(
    val title: String,
    val timeAgo: String,
    val image: ImageInfo? = null,
    val url: String,
    val source: String? = null,
    val snippet: String,
)
