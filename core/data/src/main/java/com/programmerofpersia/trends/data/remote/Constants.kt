package com.programmerofpersia.trends.data.remote

object Constants {

    // REMOTE
    const val BASE_TRENDS_URL = "https://trends.google.com/trends/"
    const val BASE_TRENDS_API_URL = "${BASE_TRENDS_URL}api/"
    const val GENERAL_URL = "${BASE_TRENDS_URL}api/explore"
    const val AUTO_COMPLETE_URL = "${BASE_TRENDS_URL}api/autocomplete"
    /*const val DAILY_TRENDS = "$BASE_TRENDS_URL/api/dailytrends"*/
    const val DAILY_TRENDS = "api/dailytrends"
    const val REAL_TIME_TRENDS = "${BASE_TRENDS_URL}api/realtimetrends"
    const val GEO_LIST = "${BASE_TRENDS_URL}api/explore/pickers/geo"
    const val CATEGORY_LIST = "${BASE_TRENDS_URL}api/explore/pickers/category"
    const val EXPLORE_DETAILS = "${BASE_TRENDS_URL}api/explore"
    const val SEARCHED_KEYWORDS = "${BASE_TRENDS_URL}api/widgetdata/relatedsearches"

    const val USER_AGENT =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.5481.77 Safari/537.36"

    //APP
    const val charsInApiResponseToBeRemoved = ")]}',"
    const val charsInGeoApiResponseToBeRemoved = ")]}'"

}