package com.programmerofpersia.trends.data.datastore

import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore<T> {

    suspend fun setValue(key: String, value: T)

    fun getValue(key: String): Flow<T?>


}