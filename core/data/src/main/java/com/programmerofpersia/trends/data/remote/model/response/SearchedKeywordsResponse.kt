package com.programmerofpersia.trends.data.remote.model.response

import com.programmerofpersia.trends.data.remote.model.dto.explore.DefaultSearchedKeywordsDto
import kotlinx.serialization.Serializable

@Serializable
data class SearchedKeywordsResponse(
    val default : DefaultSearchedKeywordsDto
)
