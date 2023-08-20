package com.programmerofpersia.trends.trending

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerofpersia.trends.data.domain.repository.TrendingRepository
import com.programmerofpersia.trends.data.remote.model.TrResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val trendingRepository: TrendingRepository
) : ViewModel() {

    var state by mutableStateOf(TrendingState())
        private set


    fun loadDailyTrends() {
        println("Trending-test: init ViewModel is called!")

        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            trendingRepository.loadDailyTrends(/*todo*/ "US").onEach { response ->
                when (response) {
                    is TrResponse.Success -> state = state.copy(
                        dailyTrendsInfo = response.result,
                        isLoading = false,
                        error = null
                    )

                    is TrResponse.Error -> state = state.copy(
                        dailyTrendsInfo = null,
                        isLoading = false,
                        error = response.message
                    )
                }
            }.collect()
        }

    }

}