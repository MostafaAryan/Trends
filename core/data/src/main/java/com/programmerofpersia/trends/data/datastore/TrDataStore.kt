package com.programmerofpersia.trends.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrDataStore(private val appContext: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "presentation-data")
    private val LOCATION_ID_DATA_KEY = stringPreferencesKey("LOCATION_ID_DATA")

    fun getLocationId(): Flow<String?> {
        val locationIdDataFlow: Flow<String?> = appContext.dataStore.data.map { preferences ->
            preferences[LOCATION_ID_DATA_KEY]
        }
        return locationIdDataFlow
    }

    suspend fun setLocationId(locationId: String) {
        appContext.dataStore.edit { mutablePreferences ->
            mutablePreferences[LOCATION_ID_DATA_KEY] = locationId
        }
    }

}