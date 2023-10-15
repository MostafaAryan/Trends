package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoAndCategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordInfo
import com.programmerofpersia.trends.data.domain.model.request.ExploreDetailParams
import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.TrApi
import com.programmerofpersia.trends.data.remote.model.TrResponse
import com.programmerofpersia.trends.data.remote.model.dto.explore.details.WidgetDto
import com.programmerofpersia.trends.data.remote.model.mappers.KeywordMapper
import com.programmerofpersia.trends.data.remote.model.mappers.toCategoryInfo
import com.programmerofpersia.trends.data.remote.model.mappers.toGeoInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

class ExploreRepositoryImpl(
    private val trApi: TrApi,
    override val apiExecutor: ApiExecutor
) : ExploreRepository {

    @Deprecated("This function is deprecated.")
    override fun loadGeoList(): Flow<TrResponse<GeoInfo>> = flow {
        emitApiResponse {
            trApi.fetchGeoList()
        }
    }.map { trResponse ->
        // map dto to domain-model (somethingInfo):
        when (trResponse) {
            is TrResponse.Success -> TrResponse.Success(trResponse.result?.toGeoInfo())
            is TrResponse.Error -> TrResponse.Error(trResponse.message)
        }
    }

    @Deprecated("This function is deprecated.")
    override fun loadCategoryList(): Flow<TrResponse<CategoryInfo>> = flow {
        emitApiResponse {
            trApi.fetchCategoryList()
        }
    }.map { trResponse ->
        // map dto to domain-model (somethingInfo):
        when (trResponse) {
            is TrResponse.Success -> TrResponse.Success(trResponse.result?.toCategoryInfo())
            is TrResponse.Error -> TrResponse.Error(trResponse.message)
        }
    }

    override fun loadGeoAndCategoryLists(): Flow<TrResponse<GeoAndCategoryInfo>> = flow<TrResponse<GeoAndCategoryInfo>> {

        coroutineScope {
            val geoList = async {
                callApi {
                    trApi.fetchGeoList()
                }
            }
            delay(100)
            val categoryList = async {
                callApi {
                    trApi.fetchCategoryList()
                }
            }

            val geoListResult = geoList.await()
            val categoryListResult = categoryList.await()

            val response = if(geoListResult is TrResponse.Success &&
                categoryListResult is TrResponse.Success &&
                geoListResult.result != null &&
                categoryListResult.result != null) {

                TrResponse.Success(
                    GeoAndCategoryInfo(
                        geoListResult.result.toGeoInfo(),
                        categoryListResult.result.toCategoryInfo()
                    )
                )

            } else {
                var errorMessage = "Geo and Category error."
                if (geoListResult is TrResponse.Error)
                    errorMessage = "$errorMessage ${geoListResult.message}."
                if (categoryListResult is TrResponse.Error)
                    errorMessage = "$errorMessage ${categoryListResult.message}."
                TrResponse.Error(errorMessage)
            }

            emit(response)
        }

    }

    override fun loadSearches(queryParams : ExploreDetailParams): Flow<TrResponse<KeywordInfo>> = flow {
        var params : String? = null
        try {
            params = Json.encodeToJsonElement(queryParams).toString()
        } catch (e: Exception) {
            TrResponse.Error("Params error: ${e.message}")
        } finally {
            if(!params.isNullOrEmpty()) {
                emitApiResponse {
                    trApi.fetchExploreDetails(params)
                }
            } else TrResponse.Error("Params error!")
        }
    }.map {
        when (it) {
            is TrResponse.Success -> {

                val searchedTopicsWidget =
                    it.result?.widgets?.firstOrNull { widget -> widget.id == WidgetDto.Type.RELATED_TOPICS.id }
                val searchedQueriesWidget =
                    it.result?.widgets?.firstOrNull { widget -> widget.id == WidgetDto.Type.RELATED_QUERIES.id }

                /* todo remove */
                println("trending:explore success")
                println("trending:widgets: ${it.result?.widgets}")
                println("trending:req: ${searchedTopicsWidget?.request}")

                if (searchedTopicsWidget == null || searchedQueriesWidget == null)
                    return@map TrResponse.Error("Explore result error.")

                coroutineScope {
                    val searchedTopicsJob = async {
                        callApi {
                            trApi.fetchSearchedKeywords(
                                searchedTopicsWidget.request,
                                searchedTopicsWidget.token
                            )
                        }
                    }
                    val searchedQueriesJob = async {
                        callApi {
                            trApi.fetchSearchedKeywords(
                                searchedQueriesWidget.request,
                                searchedQueriesWidget.token
                            )
                        }
                    }

                    val searchedTopicsResult = searchedTopicsJob.await()
                    val searchedQueriesResult = searchedQueriesJob.await()

                    /* todo remove */
                    println("trending:resA: $searchedTopicsResult")
                    println("trending:resB: $searchedQueriesResult")

                    if (searchedTopicsResult is TrResponse.Success
                        && searchedQueriesResult is TrResponse.Success
                        && !searchedTopicsResult.result?.default?.rankedList.isNullOrEmpty()
                        && !searchedQueriesResult.result?.default?.rankedList.isNullOrEmpty()
                    ) {
                        TrResponse.Success(
                            KeywordMapper(
                                searchedTopicsResult.result?.default?.rankedList!!,
                                searchedQueriesResult.result?.default?.rankedList!!,
                            ).map()
                        )
                    } else {
                        var errorMessage = "Search keyword error."
                        if (searchedTopicsResult is TrResponse.Error)
                            errorMessage = "$errorMessage ${searchedTopicsResult.message}."
                        if (searchedQueriesResult is TrResponse.Error)
                            errorMessage = "$errorMessage ${searchedQueriesResult.message}."
                        TrResponse.Error(errorMessage)
                    }

                }
            }

            is TrResponse.Error -> {
                println("trending:explore error: ${it.message}") /*todo remove*/

                it
            }
        }
    }

}