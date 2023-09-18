package com.programmerofpersia.trends.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerofpersia.trends.common.ui.FilterDialogItem
import com.programmerofpersia.trends.common.ui.model.mapper.fromBaseFilterMap
import com.programmerofpersia.trends.common.ui.model.mapper.toBaseFilterMap
import com.programmerofpersia.trends.common.ui.model.mapper.toFilterDialogItem
import com.programmerofpersia.trends.data.datastore.ExploreFilterStorage
import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.domain.model.explore.SearchDateInfo
import com.programmerofpersia.trends.data.domain.model.explore.SearchTypeInfo
import com.programmerofpersia.trends.data.domain.model.request.ExploreDetailParams
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
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

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


    fun prepareFilters() {
        loadGeoList()
        loadCategoryList()

        observeAndFinalizeFilters()
    }

    private fun loadGeoList() {
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

    private fun loadCategoryList() {
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

    private fun observeAndFinalizeFilters() {
        _state.onEach { exploreState ->
            if (exploreState.onlyFilterDataIsAvailable()) {
                // start collecting filter selection changes:
                retrieveSelectedFilters()

                // Stop collecting changes to state here:
                coroutineContext.job.cancel()
            }
        }.launchIn(viewModelScope)
    }

    private fun loadSearches(queryParams: ExploreDetailParams) {
        println("trending API2: loadSearches queryParams:${queryParams}")

        /* todo remove */
        val a = TrRemoteVariables.googleCookies
        println("trending:log: - saved cookies: $a")


        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            exploreRepository.loadSearches(queryParams).onEach { response ->

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
                            error = null
                        ).attemptHidingLoading()
                    }

                    is TrResponse.Error -> _state.update {
                        it.copy(
                            searchedTopicsList = null,
                            searchedQueriesList = null,
                            isLoading = false,
                            error = response.message
                        )
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

    private fun retrieveSelectedFilters() {
        exploreFilterStorage.retrieve().onEach {
            if (it.isNullOrEmpty()) {
                // First time opening app. No filter has been selected. Set the default filters.
                storeSelectedFilters(generateFilterDialogParentMap(_state.value, false))
            } else {
                it.fromBaseFilterMap().toMutableMap().let { selectedFiltersMap ->

                    _selectedFilters.value = selectedFiltersMap

                    val queryParams = ExploreDetailParams.create(
                        geoId = selectedFiltersMap[GeoInfo.key]?.id ?: "",
                        dateId = selectedFiltersMap[SearchDateInfo.key]?.id ?: "",
                        categoryId = selectedFiltersMap[CategoryInfo.key]?.id ?: "",
                        searchTypeId = selectedFiltersMap[SearchTypeInfo.key]?.id ?: ""
                    )
                    loadSearches(queryParams)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun generateFilterDialogParentMap(
        exploreState: ExploreState,
        includeChildObjects: Boolean = true
    ): LinkedHashMap<String, FilterDialogItem> {
        return if (exploreState.atLeastFilterDataIsAvailable()) {
            linkedMapOf(
                GeoInfo.key to exploreState.geoList!!.toFilterDialogItem(
                    includeChildObjects
                ),
                SearchDateInfo.key to SearchDateInfo.Companion.toFilterDialogItem(
                    includeChildObjects
                ),
                CategoryInfo.key to exploreState.categoryList!!.toFilterDialogItem(
                    includeChildObjects
                ),
                SearchTypeInfo.key to SearchTypeInfo.Companion.toFilterDialogItem(
                    includeChildObjects
                )
            )
        } else linkedMapOf()
    }


}