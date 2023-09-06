package com.programmerofpersia.trends.data.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TrendsLocation(val id: String, val name: String) {

    companion object {
        fun getTrendsLocationList() =
            Json.decodeFromString<List<TrendsLocation>>(TrendsLocationConstants.rawLocations)

        const val defaultLocationId = "US"
    }

}
