package com.programmerofpersia.trends.data.di

import android.app.Application
import com.programmerofpersia.trends.data.datastore.TrDataStore
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
    fun provideDataStore(application: Application): TrDataStore {
        return TrDataStore(application.applicationContext)
    }

}