package com.programmerofpersia.trends.data.remote.model.dto.explore

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(

    /*
    * Its server-side type is Int.
    * */
    @Serializable(IntAsStringSerializer::class)
    val id: String,

    val name: String,

    private val children: List<CategoryDto>? = null,

) {

    val childrenList: List<CategoryDto>
        get() = children ?: listOf()

}