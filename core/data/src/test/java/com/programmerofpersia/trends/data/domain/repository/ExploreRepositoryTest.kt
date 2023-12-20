package com.programmerofpersia.trends.data.domain.repository


import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.ApiExecutorImpl
import com.programmerofpersia.trends.data.remote.TrApi
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExploreRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var trApi: TrApi
    lateinit var apiExecutor: ApiExecutor
    lateinit var exploreRepository: ExploreRepository

    @Before
    fun setUp() {
        apiExecutor = ApiExecutorImpl()
        exploreRepository = ExploreRepositoryImpl(trApi, apiExecutor)
    }

    @Test
    fun `loadGeoAndCategoryLists() should invoke TrApi_fetchGeoList() and TrApi_fetchCategoryList()`() =
        runTest {

            exploreRepository.loadGeoAndCategoryLists().collect()

            coVerify(exactly = 1) {
                trApi.fetchGeoList()
                trApi.fetchCategoryList()
            }

        }


}