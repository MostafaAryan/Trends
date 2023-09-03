package com.programmerofpersia.trends.data.remote.model.dto.explore.details

import kotlinx.serialization.Serializable

@Serializable
data class WidgetDto(
    val id: String,
    @Serializable(RequestDetailObjectAsStringSerializer::class)
    val request: String,
    val token: String,
    val title: String
) {

    /* todo : change to polymorphic deserialization using 'id' */
    enum class Type(val index : Int, val id: String) {
        RELATED_TOPICS(0, "RELATED_TOPICS"),
        RELATED_QUERIES(1, "RELATED_QUERIES")
    }

}
