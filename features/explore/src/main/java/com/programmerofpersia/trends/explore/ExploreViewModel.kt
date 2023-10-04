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
import com.programmerofpersia.trends.data.remote.model.TrResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val _state =
        MutableStateFlow<ExploreStateHolder>(ExploreStateHolder())
    val state = _state.asStateFlow()

    private val _selectedFiltersState =
        MutableStateFlow<MutableMap<String, FilterDialogItem>>(mutableMapOf())
    val selectedFiltersState: StateFlow<Map<String, FilterDialogItem>> = _selectedFiltersState

    private val _searchKeywordState = MutableStateFlow("")
    val searchKeywordState: StateFlow<String> = _searchKeywordState

    fun prepareFilters() {
        loadGeoList()
        loadCategoryList()

        observeAndFinalizeFilters()
    }

    private fun loadGeoList() {
        viewModelScope.launch {
            /* todo loading should correlate with other api calls */
            _state.update {
                ExploreStateHolder().setState(ExploreState.Loading)
            }

            exploreRepository.loadGeoList().onEach { response ->
                when (response) {
                    is TrResponse.Success -> _state.update {
                        // it.copy(geoList = response.result).attemptHidingLoading()
                        it.copy().setState(
                            ExploreState.Success.OnlyFilterDataIsAvailable(
                                geo = response.result
                            )
                        )
                    }

                    is TrResponse.Error -> _state.update {
                        // ExploreState(error = response.message)
                        it.copy().setState(ExploreState.Error(response.message))
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
                        // it.copy(categoryList = response.result).attemptHidingLoading()
                        it.copy().setState(
                            ExploreState.Success.OnlyFilterDataIsAvailable(
                                category = response.result
                            )
                        )
                    }

                    is TrResponse.Error -> _state.update {
                        it.copy().setState(ExploreState.Error(response.message))
                    }
                }
            }.collect()
        }
    }

    private fun observeAndFinalizeFilters() {
        _state.onEach { exploreState ->
            if (exploreState.currentState() is ExploreState.Success.OnlyFilterDataIsAvailable
            ) {
                // start collecting filter selection changes:
                retrieveSelectedFilters()
                collectFilterAndSearchKeywordChanges()

                // Stop collecting changes to state here:
                coroutineContext.job.cancel()
            }
        }.launchIn(viewModelScope)
    }

    private fun loadSearches(queryParams: ExploreDetailParams) {

        viewModelScope.launch {

            _state.update {
                it.copy().setState(ExploreState.Loading)
            }

            exploreRepository.loadSearches(queryParams).onEach { response ->

                when (response) {
                    is TrResponse.Success -> _state.update {
                        /*it.copy(
                            searchedTopicsList = response.result?.topicList,
                            searchedQueriesList = response.result?.queryList,
                            error = null
                        ).attemptHidingLoading()*/

                        it.copy().setState(
                            ExploreState.Success.AllScreenDataIsAvailable(
                                searchedTopics = response.result?.topicList,
                                searchedQueries = response.result?.queryList,
                            )
                        )

                    }

                    is TrResponse.Error -> _state.update {
                        it.copy().setState(ExploreState.Error(response.message))
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

                    // Read from persistent and store in a variable as a cache.
                    _selectedFiltersState.value = selectedFiltersMap

                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateSearchKeyword(value: String) {
        _searchKeywordState.value = value
    }

    private fun observeSearchKeyword() {
        println("trending-search: observeSearchKeyword")

        searchKeywordState
            .debounce(500)
            .distinctUntilChanged()
            .onEach {
                println("trending-search: observeSearchKeyword oneach: ${it}")
            }
            .launchIn(viewModelScope)
    }

    private fun searchKeywordFlow() = searchKeywordState
        .debounce(1000)
        .distinctUntilChanged()

    /**
     * Observes changes made to filter selection and search-keyword and sends request to server
     * to update result-list accordingly.
     **/
    private fun collectFilterAndSearchKeywordChanges() {
        combine(selectedFiltersState, searchKeywordFlow()) { selectedFiltersMap, searchKeyword ->
            println("trending-search: combineFlows combine:")
            generateQueryParams(selectedFiltersMap, searchKeyword)
        }.distinctUntilChanged()
            .onEach { queryParams ->
                println("trending-search: combineFlows onEach:")
                loadSearches(queryParams)
            }.launchIn(viewModelScope)
    }

    private fun generateQueryParams(
        selectedFiltersMap: Map<String, FilterDialogItem>,
        searchKeyword: String
    ): ExploreDetailParams {
        // val selectedFiltersMap = selectedFilters.value

        return ExploreDetailParams.create(
            keyword = searchKeyword.ifBlank { null },
            geoId = selectedFiltersMap[GeoInfo.key]?.id ?: "",
            dateId = selectedFiltersMap[SearchDateInfo.key]?.id ?: "",
            categoryId = selectedFiltersMap[CategoryInfo.key]?.id ?: "",
            searchTypeId = selectedFiltersMap[SearchTypeInfo.key]?.id ?: ""
        )
    }

    fun generateFilterDialogParentMap(
        exploreState: ExploreStateHolder,
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