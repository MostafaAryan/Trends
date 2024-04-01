package com.programmerofpersia.trends.explore.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.acsbendi.requestinspectorwebview.RequestInspectorWebViewClient
import com.acsbendi.requestinspectorwebview.WebViewRequest
import com.programmerofpersia.trends.data.remote.TrRemoteVariables
import com.programmerofpersia.trends.explore.model.RelatedSearchesPayload
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.io.StringWriter
import java.net.URL


@Composable
fun MyWebView(
    context: Context,
    webViewLoadUrl: String,
    loadTopic: (searchedTopicsPayload: RelatedSearchesPayload, searchedQueriesPayload: RelatedSearchesPayload) -> Unit,
    progressBar: (visible: Boolean) -> Unit
) {


    var webView: WebView? by remember { mutableStateOf(null) }
    var isPageLoadingFinished by remember { mutableStateOf(false) }
    var previousWebViewLoadUrl by remember { mutableStateOf("") }

    LaunchedEffect(key1 = isPageLoadingFinished, key2 = webViewLoadUrl) {
        if (
            isPageLoadingFinished
            && webView != null
            && webViewLoadUrl != previousWebViewLoadUrl
        ) {
            isPageLoadingFinished = false
            previousWebViewLoadUrl = webViewLoadUrl
            webView!!.loadUrl(webViewLoadUrl)
            progressBar(true)
        }
    }


    var searchedTopicsPayload: RelatedSearchesPayload? by remember {
        mutableStateOf(null)
    }
    var searchedQueriesPayload: RelatedSearchesPayload? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = searchedTopicsPayload, key2 = searchedQueriesPayload) {
        if (searchedTopicsPayload != null && searchedQueriesPayload != null) {
            loadTopic(searchedTopicsPayload!!, searchedQueriesPayload!!)
            searchedTopicsPayload = null
            searchedQueriesPayload = null
            Log.d("TRENDS-WEBVIEW-URL:", "LaunchedEffect : load topics")
        }
    }

    AndroidView(
        modifier = Modifier.size(0.dp),
        update = { view ->
            webView = view
        },
        factory = {
            WebView(context).apply {

                // SET USER AGENT
                val webviewUserAgent = settings.userAgentString
                TrRemoteVariables.webviewUserAgent = webviewUserAgent
                Log.d("TRENDS-WEBVIEW-URL", "webview useragent:$webviewUserAgent")

                // RequestInspectorWebViewClient is used to intercept POST requests.
                webViewClient = object : RequestInspectorWebViewClient(this@apply) {

                    private var flag = false
                    private var checker = 0
                    private var result = 0


                    override fun shouldInterceptRequest(
                        view: WebView,
                        webViewRequest: WebViewRequest
                    ): WebResourceResponse? {

                        if (webViewRequest.url.contains("relatedsearches")) {
                            Log.d("TRENDS-WEBVIEW-URL:", "url is relatedSearches")
                            Log.d("TRENDS-WEBVIEW-URL:", "url:${webViewRequest.url}")
                            val uri = Uri.parse(webViewRequest.url)
                            val req = uri.getQueryParameter("req") ?: ""
                            val token = uri.getQueryParameter("token") ?: ""
                            if (req.contains("ENTITY")) {
                                searchedTopicsPayload = RelatedSearchesPayload(token, req)
                            } else if (req.contains("QUERY")) {
                                searchedQueriesPayload = RelatedSearchesPayload(token, req)
                            } else {
                                Log.e("TRENDS-WEBVIEW-URL:", "related searches URI error!")
                            }
                            Log.d("TRENDS-WEBVIEW-URL:", "req:$req")
                            Log.d("TRENDS-WEBVIEW-URL:", "token:$token")
                        }

                        if (webViewRequest.url.contains("https://trends.google.com/trends/api/explore")
                            && webViewRequest.method == "POST"
                        ) {

                            /* ______ */

                            val url = webViewRequest.url
                            try {
                                val aURL = URL(url)
                                val conn = aURL.openConnection()
                                conn.connect()
                                val `is` = conn.getInputStream()
                                val writer = StringWriter()
                                IOUtils.copy(`is`, writer, "UTF-8")
                                Log.e(
                                    "WEBVIEW_NEW_SOLUTION",
                                    "shouldInterceptRequest: string $writer"
                                )
                                val `object` = JSONObject(writer.toString())
                                Log.e(
                                    "WEBVIEW_NEW_SOLUTION",
                                    "shouldInterceptRequest: json object $`object`"
                                )

                                return super.shouldInterceptRequest(view, webViewRequest)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Log.e(
                                    "WEBVIEW_NEW_SOLUTION",
                                    "shouldInterceptRequest: exception " + e.message
                                )
                            }


                            /* ______ */

                            println("TRENDS-WEBVIEW-URL: {${webViewRequest?.url}}")
                            println("TRENDS-WEBVIEW-HEADAER: {${webViewRequest?.headers}}")
                            println("TRENDS-WEBVIEW-METHOD: {${webViewRequest?.method}}")
                            println("TRENDS-WEBVIEW-BODY: {${webViewRequest?.body}}")
                            println("TRENDS-WEBVIEW-FORM: {${webViewRequest?.formParameters}}")

                            // Body
                            // val requestBody = webViewRequest.body

                            // Header
                            println(
                                "TRENDS-WEBVIEW-COOKIE-GRAB: hasCookie {${
                                    webViewRequest.headers.contains(
                                        "cookie"
                                    )
                                }}"
                            )
                            if (webViewRequest.headers.contains("cookie")) {
                                // val cookie = webViewRequest.headers["cookie"]
                            }

                        }


                        return super.shouldInterceptRequest(view, webViewRequest)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d("TRENDS-WEBVIEW-URL", "onPageFinished url: $url")

                        // Store cookies
                        val cookies = CookieManager.getInstance().getCookie(url)
                        Log.d("TRENDS-WEBVIEW-URL", "COOKIES:$cookies")
                        TrRemoteVariables.webviewCookie = cookies

                        isPageLoadingFinished = true
                        progressBar(false)

                    }

                }

                // settings.javaScriptEnabled = true
                Log.d("TRENDS-WEBVIEW-URL", "Loading page A...")
                loadUrl("https://trends.google.com/trends")
                progressBar(true)
            }
        }
    )
}
