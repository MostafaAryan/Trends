package com.programmerofpersia.trends.data.domain.model.explore

data class CategoryInfo(
    val children: List<CategoryInfo>,
    val name: String,
    val id: Int,
)
