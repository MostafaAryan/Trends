package com.programmerofpersia.trends.data.datastore

import com.programmerofpersia.trends.data.domain.model.explore.BaseFilterInfo

class ExploreFilterStorageImpl(
    override val dataStore: TrDataStore.SerializedDataStore<Map<String, BaseFilterInfo>>
) : ExploreFilterStorage {

    override val key: String = "EXPLORE_FILTER_SELECTIONS_DATA"

    override suspend fun store(value: Map<String, BaseFilterInfo>) = dataStore.setValue(value)

    override fun retrieve() = dataStore.getValue()
}