package com.programmerofpersia.trends.data.remote

import com.programmerofpersia.trends.data.remote.model.TrResponse
import retrofit2.Response

class ApiExecutorImpl : ApiExecutor {

    override suspend fun <T> executeApi(request: suspend () -> Response<T>): TrResponse<T> {

        // Invoke api request using retrofit:
        val result = request.invoke()

        return if (result.isSuccessful) {
            TrResponse.Success(result.body())
        } else {
            TrResponse.Error("Trending:Error in api execution!")
        }
    }

}