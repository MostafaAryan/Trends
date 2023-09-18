package com.programmerofpersia.trends.data.domain.model.explore

import kotlinx.serialization.SerialName


data class SearchTypeInfo(

    @SerialName("property")
    override val id: String,

    @SerialName("title")
    override val name: String

) : BaseFilterInfo {

    companion object {

        private val webSearch = SearchTypeInfo("", "Web Search")
        private val youTubeSearch = SearchTypeInfo("youtube", "YouTube Search")
        private val imageSearch = SearchTypeInfo("images", "Image Search")
        private val googleShopping = SearchTypeInfo("froogle", "Google Shopping")

        fun getAllSearchTypesAsList() = listOf(
            webSearch,
            youTubeSearch,
            imageSearch,
            googleShopping
        )

        /* todo improve implementation to avoid not-null symbol. */
        val key = SearchTypeInfo::class.simpleName!!
    }

}