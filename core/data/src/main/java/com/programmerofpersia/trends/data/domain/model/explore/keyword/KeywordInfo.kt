package com.programmerofpersia.trends.data.domain.model.explore.keyword

data class KeywordInfo(
    val topicList: List<KeywordTopicInfo>,
    val queryList: List<KeywordQueryInfo>,
)

data class KeywordTopicInfo(
    val topic: TopicInfo,
    override val value: Int,
    override val formattedValue: String,
    override val link: String,
) : BaseKeywordInfo

data class KeywordQueryInfo(
    val query: String,
    override val value: Int,
    override val formattedValue: String,
    override val link: String,
) : BaseKeywordInfo
