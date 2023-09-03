package com.programmerofpersia.trends.data.remote.model.mappers

import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordQueryInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordTopicInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.TopicInfo
import com.programmerofpersia.trends.data.remote.model.dto.explore.KeywordDto
import com.programmerofpersia.trends.data.remote.model.dto.explore.RankedListItemDto

class KeywordMapper(
    private val searchedTopics: List<RankedListItemDto>,
    private val searchedQueries: List<RankedListItemDto>
) : Mapper<KeywordInfo> {

    override fun map(): KeywordInfo {
        val risingTopics =
            searchedTopics[RankedListItemDto.ItemTypeIndex.RISING.index].rankedKeyword
        val risingQueries =
            searchedQueries[RankedListItemDto.ItemTypeIndex.RISING.index].rankedKeyword

        return KeywordInfo(
            risingTopics.map {
                it as KeywordDto.Topic
                KeywordTopicInfo(
                    TopicInfo(
                        it.topic.mid,
                        it.topic.title,
                        it.topic.type,
                    ),
                    it.value,
                    it.formattedValue,
                    it.link
                )
            },
            risingQueries.map {
                it as KeywordDto.Query
                KeywordQueryInfo(
                    it.query,
                    it.value,
                    it.formattedValue,
                    it.link
                )
            }
        )
    }


}