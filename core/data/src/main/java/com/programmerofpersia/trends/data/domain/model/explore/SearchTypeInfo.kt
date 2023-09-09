package com.programmerofpersia.trends.data.domain.model.explore


data class SearchTypeInfo(val property: String, val title: String) {

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
    }

}