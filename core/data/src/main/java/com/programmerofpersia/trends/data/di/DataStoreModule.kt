package com.programmerofpersia.trends.data.di

import android.app.Application
import com.programmerofpersia.trends.data.datastore.ExploreFilterDataStoreImpl
import com.programmerofpersia.trends.data.datastore.ExploreFilterStorage
import com.programmerofpersia.trends.data.datastore.ExploreFilterStorageImpl
import com.programmerofpersia.trends.data.datastore.LocationStorage
import com.programmerofpersia.trends.data.datastore.LocationStorageImpl
import com.programmerofpersia.trends.data.datastore.PreferencesDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideLocationStorage(application: Application): LocationStorage {
        return LocationStorageImpl(PreferencesDataStoreImpl(application.applicationContext))
    }

    @Provides
    @Singleton
    fun provideExploreFilterStorage(application: Application): ExploreFilterStorage {
        return ExploreFilterStorageImpl(ExploreFilterDataStoreImpl(application.applicationContext))
    }

}