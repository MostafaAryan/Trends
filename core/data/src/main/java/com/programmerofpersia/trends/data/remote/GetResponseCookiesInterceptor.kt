package com.programmerofpersia.trends.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpCookie

class GetResponseCookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (originalResponse.request.url.toString() == Constants.BASE_TRENDS_URL &&
            originalResponse.headers("Set-Cookie").isNotEmpty()
        ) {
            Log.d("trending:cookie-body:", "cookie collection url: ${originalResponse.request.url}") /* todo remove */

            val cookies = hashSetOf<String>()
            originalResponse.headers("Set-Cookie").forEach {
                cookies.add(it)
                Log.d("trending:cookie-body:", "cookie: $it") /* todo remove */

                val cookieItemList : List<HttpCookie> = HttpCookie.parse(it)
                cookieItemList.forEach {cookieItem ->
                    Log.d("trending:cookie-body:", "cookieItem: $cookieItem") /* todo remove */
                    if(cookieItem.toString().contains("NID")) {
                        // cookies.add(cookieItem.toString())
                        // Log.d("trending:cookie-body:", "cookieItem added: ${cookieItem.toString()}") /* todo remove */
                    }
                }

            }

            /* todo store in data store */
            TrRemoteVariables.googleCookies.clear()
            TrRemoteVariables.googleCookies.addAll(cookies)
        }

        return originalResponse
    }

}