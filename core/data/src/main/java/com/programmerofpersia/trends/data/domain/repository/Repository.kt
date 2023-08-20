package com.programmerofpersia.trends.data.domain.repository

import com.programmerofpersia.trends.data.remote.ApiExecutor
import com.programmerofpersia.trends.data.remote.model.TrResponse
import kotlinx.coroutines.flow.FlowCollector
import retrofit2.Response

interface Repository {

    val apiExecutor: ApiExecutor

    suspend fun <T> FlowCollector<TrResponse<T>>.emitApiResponse(request: suspend () -> Response<T>) {
        val apiResponse = apiExecutor.executeApi(request)
        emit(apiResponse)
    }

}