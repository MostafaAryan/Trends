package com.programmerofpersia.trends.data.domain.model.explore

data class CategoryInfo(
    val children: List<CategoryInfo>,
    override val name: String,
    override val id: String,
) : BaseFilterInfo
