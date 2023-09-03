package com.programmerofpersia.trends.data.domain.model.explore.keyword

interface BaseKeywordInfo {
    val value: Int
    val formattedValue: String
    val link: String

    // Used for 'Top' mode:
    // val hasData : Boolean
}