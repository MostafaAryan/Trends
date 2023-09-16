package com.programmerofpersia.trends.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.programmerofpersia.trends.data.domain.model.explore.BaseFilterInfo
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExploreFilterDataStoreImpl(
    private val appContext: Context
) : TrDataStore.SerializedDataStore<Map<String, BaseFilterInfo>> {

    /* todo change to string constant */
    private val Context.dataStore by dataStore("explore-filter-data.json", ExploreFilterSerializer)

    override suspend fun setValue(value: Map<String, BaseFilterInfo>) {
        appContext.dataStore.updateData { exploreFilter ->
            exploreFilter.copy(
                persistentMap = value.toPersistentMap()
            )
        }
    }

    override fun getValue(): Flow<Map<String, BaseFilterInfo>?> {
        return appContext.dataStore.data.map { it.persistentMap }
    }


}