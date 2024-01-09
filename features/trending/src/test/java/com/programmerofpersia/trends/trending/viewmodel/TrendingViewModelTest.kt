package com.programmerofpersia.trends.trending.viewmodel

import com.programmerofpersia.trends.data.datastore.LocationStorage
import com.programmerofpersia.trends.data.domain.TrendsLocation
import com.programmerofpersia.trends.data.domain.repository.fake.FakeTrendingRepositoryImpl
import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.trending.MainCoroutineRule
import com.programmerofpersia.trends.trending.screenstate.TrendingState
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(/*UnconfinedTestDispatcher()*/)

    @RelaxedMockK
    lateinit var apiExecutor: ApiExecutor

    @RelaxedMockK
    lateinit var locationStorage: LocationStorage

    private lateinit var viewModel: TrendingViewModel

    @Before
    fun setUp() {
        viewModel = TrendingViewModel(
            FakeTrendingRepositoryImpl(apiExecutor),
            locationStorage
        )
    }

    @Test
    fun `state should be successful when loadDailyTrends() is called with a valid location`() =
        runTest {
            every { locationStorage.retrieve() } returns flow {
                emit(TrendsLocation.DEFAULT_LOCATION_ID)
            }

            viewModel.retrieveSelectedLocation()

            /** This will call [TrendingViewModel.loadDailyTrends] **/
            viewModel.handleLocationChanges()

            // As we are testing coroutines, we should use
            advanceUntilIdle()
            // or delay(500)
            // or MainCoroutineRule(UnconfinedTestDispatcher())

            Assert.assertEquals(
                TrendingState(
                    dailyTrendsInfo = FakeTrendingRepositoryImpl.fakeDailyTrendsInfo,
                    isLoading = false,
                    error = null
                ),
                viewModel.state
            )
        }

    @Test
    fun `state should be error when loadDailyTrends() is called with empty location`() = runTest {
        every { locationStorage.retrieve() } returns flow {
            emit("")
        }

        viewModel.retrieveSelectedLocation()

        /** This will call [TrendingViewModel.loadDailyTrends] **/
        viewModel.handleLocationChanges()

        advanceUntilIdle()

        Assert.assertNull(viewModel.state.dailyTrendsInfo)
        Assert.assertFalse(viewModel.state.isLoading)
        Assert.assertNotNull(viewModel.state.error) // There should be an error message
    }

}