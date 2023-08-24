package com.programmerofpersia.trends.trending

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerofpersia.trends.data.datastore.TrDataStore
import com.programmerofpersia.trends.data.domain.TrendsLocation
import com.programmerofpersia.trends.data.domain.repository.TrendingRepository
import com.programmerofpersia.trends.data.remote.model.TrResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val trendingRepository: TrendingRepository,
    private val dataStore: TrDataStore
) : ViewModel() {

    private var _selectedLocationId = MutableStateFlow<String?>(null)
    var selectedLocationId: StateFlow<String?> = _selectedLocationId

    val countryList = TrendsLocation.getTrendsLocationList()

    var selectedCountry: TrendsLocation? by mutableStateOf(null)
        private set
    var state by mutableStateOf(TrendingState())
        private set

    fun retrieveSelectedLocation() {
        viewModelScope.launch {
            dataStore.getLocationId().onEach { locationId ->
                _selectedLocationId.value = locationId
                selectedCountry = countryList.find { country -> country.id == locationId }
            }.collect()
        }
    }

    fun updateSelectedLocationId(locationId: String) {
        viewModelScope.launch { dataStore.setLocationId(locationId) }
    }

    fun handleLocationChanges() {
        selectedLocationId.onEach { locationId ->
            if (locationId != null) loadDailyTrends(locationId)
        }.launchIn(viewModelScope)
    }

    private fun loadDailyTrends(locationId: String) {

        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )

            trendingRepository.loadDailyTrends(locationId)
                .onEach { response ->
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