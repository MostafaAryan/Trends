package com.programmerofpersia.trends.data.di

import com.programmerofpersia.trends.data.domain.repository.ExploreRepository
import com.programmerofpersia.trends.data.domain.repository.ExploreRepositoryImpl
import com.programmerofpersia.trends.data.domain.repository.TrendingRepository
import com.programmerofpersia.trends.data.domain.repository.TrendingRepositoryImpl
import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.TrApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTrendingRepository(trApi: TrApi, apiExecutor: ApiExecutor): TrendingRepository {
        return TrendingRepositoryImpl(trApi, apiExecutor)
    }

    @Provides
    @Singleton
    fun provideExploreRepository(trApi: TrApi, apiExecutor: ApiExecutor): ExploreRepository {
        return ExploreRepositoryImpl(trApi, apiExecutor)
    }

}