package com.programmerofpersia.trends.data.remote.model.dto.explore

import kotlinx.serialization.Serializable

@Serializable
data class DefaultSearchedKeywordsDto(
    val rankedList: List<RankedListItemDto>
)
