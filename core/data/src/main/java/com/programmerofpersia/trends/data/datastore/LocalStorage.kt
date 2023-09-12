package com.programmerofpersia.trends.data.datastore

import kotlinx.coroutines.flow.Flow

interface LocalStorage<T> {

    val dataStore : PreferencesDataStore<T>

    val key : String

    suspend fun store(value: T)

    fun retrieve(): Flow<T?>

}