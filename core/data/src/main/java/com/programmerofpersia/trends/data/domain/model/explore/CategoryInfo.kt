package com.programmerofpersia.trends.data.domain.model.explore

data class CategoryInfo(
    val children: List<CategoryInfo>,
    override val name: String,
    override val id: String,
) : BaseFilterInfo {

    companion object {
        /* todo improve implementation to avoid not-null symbol. */
        val key = CategoryInfo::class.simpleName!!
    }

}
