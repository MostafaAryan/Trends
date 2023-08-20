package com.programmerofpersia.trends

import androidx.annotation.StringRes

/* todo: change label to @StringRes */
@Deprecated(message = "This will soon be deleted.")
sealed class Screen(val route : String, val label : String) {
    object TrendingNowGraph : Screen("trending-now-graph", "Trending now")
    object ExploreGraph : Screen("explore-graph","Explore")
    object TrendingNowScreen : Screen("trending-now-screen", "Trending now")
    object ExploreScreen : Screen("explore-screen","Explore")
}
