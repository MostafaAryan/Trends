package com.programmerofpersia.trends.data.remote.model.dto.explore

import kotlinx.serialization.Serializable

@Serializable(SearchedKeywordSerializer::class)
sealed class KeywordDto {
    abstract val value: Int
    abstract val formattedValue: String
    abstract val link: String
    // Used for 'Top' mode:
    // val hasData : Boolean

    @Serializable
    data class Topic(
        val topic: TopicDto,
        override val value: Int,
        override val formattedValue: String,
        override val link: String,
    ) : KeywordDto()

    @Serializable
    data class Query(
        val query: String,
        override val value: Int,
        override val formattedValue: String,
        override val link: String,
    ) : KeywordDto()
}
