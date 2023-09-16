package com.programmerofpersia.trends.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDataStoreImpl<T>(
    private val appContext: Context,
    private val klass: Class<T>
) : TrDataStore.PreferencesDataStore<T> {

    /* todo replace with constant string */
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "presentation-data")


    companion object {
        inline operator fun <reified T : Any> invoke(appContext: Context) =
            PreferencesDataStoreImpl(appContext, T::class.java)
    }


    override suspend fun setValue(key: String, value: T) {
        when {
            klass.isAssignableFrom(String::class.java) && value is String -> {
                appContext.dataStore.edit { mutablePreferences ->
                    mutablePreferences[stringPreferencesKey(key)] = value
                }
            }

            else -> {
                throw Exception("Trending exception: Incorrect type exception.")
            }
        }
    }

    override fun getValue(key: String): Flow<T?> {
        when {
            klass.isAssignableFrom(String::class.java) -> {
                val locationIdDataFlow: Flow<String?> =
                    appContext.dataStore.data.map { preferences ->
                        preferences[stringPreferencesKey(key)]
                    }
                return locationIdDataFlow as Flow<T?>
            }

            else -> {
                throw Exception("Trending exception: Incorrect type exception.")
            }
        }
    }


    /* todo : remove */
    private class PreferenceTypeDecider<T : Any>(val klass: Class<T>) {
        companion object {
            inline operator fun <reified T : Any> invoke() = PreferenceTypeDecider(T::class.java)
        }

        fun decide(t: Any) = when {
            klass.isAssignableFrom(String::class.java) -> stringPreferencesKey("")
            else -> {
                intPreferencesKey("")
            }
        }

    }

    fun test() {
        PreferenceTypeDecider<String>().decide("foo")
        PreferenceTypeDecider<String>().decide(1)
    }


}