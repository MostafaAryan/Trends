package com.programmerofpersia.trends.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerofpersia.trends.common.ui.FilterDialogItem
import com.programmerofpersia.trends.common.ui.model.mapper.fromBaseFilterMap
import com.programmerofpersia.trends.common.ui.model.mapper.toBaseFilterMap
import com.programmerofpersia.trends.data.datastore.ExploreFilterStorage
import com.programmerofpersia.trends.data.domain.repository.ExploreRepository
import com.programmerofpersia.trends.data.remote.TrRemoteVariables
import com.programmerofpersia.trends.data.remote.model.TrResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val exploreRepository: ExploreRepository,
    private val exploreFilterStorage: ExploreFilterStorage
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreState())
    val state = _state.asStateFlow()

    private val _selectedFilters =
        MutableStateFlow<MutableMap<String, FilterDialogItem>>(mutableMapOf())
    val selectedFilters: StateFlow<Map<String, FilterDialogItem>> = _selectedFilters

    fun loadGeoList() {
        viewModelScope.launch {
            /* todo loading should correlate with other api calls */
            _state.update {
                ExploreState(
                    isLoading = true
                )
            }

            exploreRepository.loadGeoList().onEach { response ->
                when (response) {
                    is TrResponse.Success -> _state.update {
                        it.copy(geoList = response.result).attemptHidingLoading()
                    }

                    is TrResponse.Error -> _state.update {
                        ExploreState(error = response.message)
                    }
                }
            }.collect()
        }
    }

    fun loadCategoryList() {
        viewModelScope.launch {
            exploreRepository.loadCategoryList().onEach { response ->
                when (response) {
                    is TrResponse.Success -> _state.update {
                        it.copy(categoryList = response.result).attemptHidingLoading()
                    }

                    is TrResponse.Error -> _state.update {
                        ExploreState(error = response.message)
                    }
                }
            }.collect()
        }
    }

    fun loadSearches() {
        /* todo remove */
        val a = TrRemoteVariables.googleCookies
        println("trending:log: - saved cookies: $a")

        viewModelScope.launch {
            exploreRepository.loadSearches().onEach { response ->

                /*todo remove*/
                if (response is TrResponse.Success) {
                    println("Explore viewmodel: loadsearches: ${response.result?.topicList}")
                    println("Explore viewmodel: loadsearches: ${response.result?.queryList}")
                }

                when (response) {
                    is TrResponse.Success -> _state.update {
                        it.copy(
                            searchedTopicsList = response.result?.topicList,
                            searchedQueriesList = response.result?.queryList,
                        ).attemptHidingLoading()
                    }

                    is TrResponse.Error -> _state.update {
                        ExploreState(error = response.message)
                    }
                }

            }.collect()
        }
    }

    fun storeSelectedFilters(filterSelectionMap: Map<String, FilterDialogItem>) {
        viewModelScope.launch {
            exploreFilterStorage.store(filterSelectionMap.toBaseFilterMap())
        }
    }

    fun retrieveSelectedFilters() {
        exploreFilterStorage.retrieve().onEach {
            if (it != null) {
                _selectedFilters.value.clear()
                _selectedFilters.value.putAll(it.fromBaseFilterMap())
            }
        }.launchIn(viewModelScope)
    }


}