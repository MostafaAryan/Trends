package com.programmerofpersia.trends.data.datastore

import kotlinx.coroutines.flow.Flow


interface TrDataStore<T> {

    interface PreferencesDataStore<T> : TrDataStore<T> {

        suspend fun setValue(key: String, value: T)

        fun getValue(key: String): Flow<T?>

    }

    interface SerializedDataStore<T> : TrDataStore<T> {

        suspend fun setValue(value: T)

        fun getValue(): Flow<T?>

    }


}



