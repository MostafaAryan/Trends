package com.programmerofpersia.trends.data.datastore.model

import com.programmerofpersia.trends.data.domain.model.explore.BaseFilterInfo
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.Serializable

@Serializable
data class ExploreFilter(
    @Serializable(with = PersistentMapSerializer::class)
    val persistentMap: PersistentMap<String, BaseFilterInfo> = persistentMapOf()
)


