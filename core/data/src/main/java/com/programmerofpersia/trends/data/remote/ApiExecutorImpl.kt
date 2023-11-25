package com.programmerofpersia.trends.data.remote

import com.programmerofpersia.trends.data.remote.model.TrResponse
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

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
            /*
            * We should not prevent coroutine's cancellation exception from being thrown.
            * Therefore, inside a suspend function or a coroutine scope, either check the exception's
            * type and throw it if it was a cancellationException, or only catch specific exceptions
            * (e.g.: NumberFormatException, IOException, etc.) and leave other exceptions to be thrown.
            *
            * Throwing cancellationException notifies super functions and super coroutine scopes
            * that the coroutine has been cancelled.
            * */
            if(e is CancellationException) throw e

            TrResponse.Error("Trending: ${e.message}")
        }
    }

}