package com.programmerofpersia.trends.data.remote

object TrRemoteVariables {

    var webviewCookie: String? = null
    var webviewUserAgent: String = "Mozilla/5.0 (Linux; Android 9; Mi A2 Build/PKQ1.180904.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/115.0.5790.166 Mobile Safari/537.36"

    /* todo move to data store */
    var googleCookies: HashSet<String> = hashSetOf()

}