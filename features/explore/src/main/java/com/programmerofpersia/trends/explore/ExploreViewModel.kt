package com.programmerofpersia.trends.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programmerofpersia.trends.data.domain.repository.ExploreRepository
import com.programmerofpersia.trends.data.remote.model.TrResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val exploreRepository: ExploreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExploreState())
    val state = _state.asStateFlow()

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
                        ExploreState(
                            geoList = response.result
                        )
                    }

                    is TrResponse.Error -> _state.update {
                        ExploreState(
                            error = response.message
                        )
                    }
                }
            }.collect()
        }
    }

}