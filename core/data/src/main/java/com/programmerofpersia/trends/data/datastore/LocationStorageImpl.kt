package com.programmerofpersia.trends.data.datastore

import kotlinx.coroutines.flow.Flow

class LocationStorageImpl(
    override val dataStore: TrDataStore.PreferencesDataStore<String>
) : LocationStorage {

    /* todo replace with constant string */
    override val key: String = "LOCATION_ID_DATA"

    override suspend fun store(value: String) = dataStore.setValue(key, value)

    override fun retrieve(): Flow<String?> = dataStore.getValue(key)


}