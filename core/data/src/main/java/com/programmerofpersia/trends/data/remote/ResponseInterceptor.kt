package com.programmerofpersia.trends.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val body = response.body
        val bodyString = body?.string()

        val newResponse = removeRedundantCharacters(body, bodyString, response)

        logResponseBody(bodyString)

        return newResponse
    }

    private fun removeRedundantCharacters(
        body: ResponseBody?,
        bodyString: String?,
        response: Response
    ): Response {
        return bodyString.run {
            if (!isNullOrEmpty() && take(6).contains(Constants.charsInApiResponseToBeRemoved)) {
                val newBody = replace(Constants.charsInApiResponseToBeRemoved, "")
                    .toResponseBody(body?.contentType())

                response.newBuilder().body(newBody).build()
            } else if (!isNullOrEmpty() && take(6).contains(Constants.charsInGeoApiResponseToBeRemoved)) {
                val newBody = replace(Constants.charsInGeoApiResponseToBeRemoved, "")
                    .toResponseBody(body?.contentType())

                response.newBuilder().body(newBody).build()
            } else response
        }

    }

    private fun logResponseBody(bodyString: String?) {
        /* todo if(appconfig.isdebug) */
        Log.d("trending:response-body:", bodyString ?: "")
    }

}