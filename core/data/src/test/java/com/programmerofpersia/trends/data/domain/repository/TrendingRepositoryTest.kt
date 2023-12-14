package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.ApiExecutorImpl
import com.programmerofpersia.trends.data.remote.TrApi
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TrendingRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var trApi: TrApi
    lateinit var apiExecutor: ApiExecutor
    lateinit var trendingRepository: TrendingRepository

    @Before
    fun setUp() {
        apiExecutor = ApiExecutorImpl()
        trendingRepository = TrendingRepositoryImpl(trApi, apiExecutor)
    }

    @Test
    fun `loadCookiesFromGoogleTrends() should invoke TrApi_callGoogleTrends()`() = runTest {

        trendingRepository.loadCookiesFromGoogleTrends()

        coVerify(exactly = 1) {
            trApi.callGoogleTrends(any())
        }

    }

    @Test
    fun `loadDailyTrends() should invoke TrApi_fetchDailyTrends()`() = runTest {

        trendingRepository.loadDailyTrends("US").single()

        coVerify(exactly = 1) {
            trApi.fetchDailyTrends(any())
        }

    }

}