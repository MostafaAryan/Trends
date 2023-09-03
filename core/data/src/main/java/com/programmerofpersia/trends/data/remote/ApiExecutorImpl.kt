package com.programmerofpersia.trends.data.remote

import com.programmerofpersia.trends.data.remote.model.TrResponse
import retrofit2.Response

class ApiExecutorImpl : ApiExecutor {

    override suspend fun <T> executeApi(request: suspend () -> Response<T>): TrResponse<T> {

        return try {
            // Invoke api request using retrofit:
            val result = request.invoke()

            if (result.isSuccessful)
                TrResponse.Success(result.body())
            else
                TrResponse.Error("Trending: Error in api execution!")

        } catch (e: Exception) {
            TrResponse.Error("Trending: ${e.message}")
        }
    }

}