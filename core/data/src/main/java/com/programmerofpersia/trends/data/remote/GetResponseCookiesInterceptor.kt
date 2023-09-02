package com.programmerofpersia.trends.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class GetResponseCookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        Log.d("trending:cookie-body:", "cookie collection url: ${originalResponse.request.url}") /* todo remove */

        if (originalResponse.request.url.toString() == Constants.BASE_TRENDS_URL &&
            originalResponse.headers("Set-Cookie").isNotEmpty()
        ) {
            val cookies = hashSetOf<String>()
            originalResponse.headers("Set-Cookie").forEach {
                cookies.add(it)
                Log.d("trending:cookie-body:", "cookie: $it") /* todo remove */
            }

            /* todo store in data store */
            TrRemoteVariables.googleCookies.clear()
            TrRemoteVariables.googleCookies.addAll(cookies)
        }

        return originalResponse
    }

}