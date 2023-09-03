package com.programmerofpersia.trends.data.remote.model.dto.explore

import kotlinx.serialization.Serializable

@Serializable
data class RankedListItemDto(
    val rankedKeyword: List<KeywordDto>
) {

    enum class ItemTypeIndex(val index: Int) {
        TOP(0),
        RISING(1)
    }

}
