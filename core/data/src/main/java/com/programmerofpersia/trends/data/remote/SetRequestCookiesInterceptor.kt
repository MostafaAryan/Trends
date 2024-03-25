package com.programmerofpersia.trends.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class SetRequestCookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        println("trending: - request url to include cookie: ${chain.request().url}") /* todo remove */

        /* This is the original code , todo remove*/
        /*if (chain.request().url.toString().contains(Constants.BASE_TRENDS_API_URL)) {
            val cookies = TrRemoteVariables.googleCookies *//* todo read from datastore *//*
            cookies.forEach {
                requestBuilder.addHeader("Cookie", it)
            }
        }*/

        // Try to use cookie from WebView, if not possible use Google API cookie.
        if (chain.request().url.toString().contains(Constants.BASE_TRENDS_API_URL)) {
            if (!TrRemoteVariables.webviewCookie.isNullOrEmpty()) {

                val cookieString = TrRemoteVariables.webviewCookie
                if (!cookieString.isNullOrEmpty()) {
                    requestBuilder.addHeader("Cookie", cookieString)
                }

            } else {

                val cookies = TrRemoteVariables.googleCookies
                cookies.forEach {
                    requestBuilder.addHeader("Cookie", it)
                }

            }
        }

        return chain.proceed(requestBuilder.build())
    }

}