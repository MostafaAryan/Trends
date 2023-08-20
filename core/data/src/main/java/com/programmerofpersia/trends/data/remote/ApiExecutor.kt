package com.programmerofpersia.trends.data.remote

import com.programmerofpersia.trends.data.remote.model.TrResponse
import retrofit2.Response


interface ApiExecutor {

    suspend fun <T> executeApi(request: suspend () -> Response<T>) : TrResponse<T>

}