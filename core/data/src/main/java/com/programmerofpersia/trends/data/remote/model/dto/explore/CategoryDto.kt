package com.programmerofpersia.trends.data.remote.model.dto.explore

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    private val children: List<CategoryDto>? = null,
    val name: String,
    val id: Int,
) {

    val childrenList: List<CategoryDto>
        get() = children ?: listOf()

}