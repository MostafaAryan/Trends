package com.programmerofpersia.trends.explore.screenstate

import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordQueryInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordTopicInfo


data class ExploreStateHolder private constructor(
    private var exploreState: ExploreState = ExploreState.Loading.FullScreen,
    //
    private var _geoList: GeoInfo? = null,
    private var _categoryList: CategoryInfo? = null,
    //
    private var _searchedTopicsList: List<KeywordTopicInfo>? = null,
    private var _searchedQueriesList: List<KeywordQueryInfo>? = null
) {

    constructor() : this(ExploreState.Loading.FullScreen)

    val geoList: GeoInfo?
        get() = _geoList

    val categoryList: CategoryInfo?
        get() = _categoryList

    val searchedTopicsList: List<KeywordTopicInfo>?
        get() = _searchedTopicsList

    val searchedQueriesList: List<KeywordQueryInfo>?
        get() = _searchedQueriesList


    /* todo : return immutable state */
    fun currentState() = exploreState
    fun setState(inputExploreState: ExploreState): ExploreStateHolder {
        exploreState = inputExploreState

        when (inputExploreState) {
            is ExploreState.Success.OnlyFilterDataIsAvailable -> {
                if (inputExploreState.geo != null)
                    _geoList = inputExploreState.geo

                if (inputExploreState.category != null)
                    _categoryList = inputExploreState.category

                // searchedTopicsList = null
                // searchedQueriesList = null
            }

            is ExploreState.Success.AllScreenDataIsAvailable -> {
                _searchedTopicsList = inputExploreState.searchedTopics
                _searchedQueriesList = inputExploreState.searchedQueries
            }

            else -> {}
        }

        /* todo remove */
        println("trending-state-test: set state: inputState  $inputExploreState")
        println("trending-state-test: set state: geoList  $_geoList")
        println("trending-state-test: set state: categoryList  $_categoryList")
        println("trending-state-test: set state: searchedTopicsList  $_searchedTopicsList")
        println("trending-state-test: set state: searchedQueriesList  $_searchedQueriesList")

        return this@ExploreStateHolder
    }

    fun atLeastFilterDataIsAvailable() =
        _geoList != null
                && _categoryList != null

}

sealed class ExploreState {

    /**
     *  Success State:
     **/
    sealed class Success : ExploreState() {

        data class OnlyFilterDataIsAvailable(
            val geo: GeoInfo? = null,
            val category: CategoryInfo? = null,
        ) : Success()

        data class AllScreenDataIsAvailable(
            val searchedTopics: List<KeywordTopicInfo>? = null,
            val searchedQueries: List<KeywordQueryInfo>? = null,
        ) : Success()

    }

    /**
     *  Loading State:
     **/
    sealed class Loading : ExploreState() {
        object FullScreen : Loading()

        object SearchField : Loading()
    }

    /**
     *  Error State:
     **/
    data class Error(val message: String? = null) : ExploreState()
}

