package com.programmerofpersia.trends.trending

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.programmerofpersia.trends.common.navigation.TrNavGraph

sealed class TrendingGraphDestinations() : TrNavGraph {

    object TrendingNowGraph : TrendingGraphDestinations() {
        override val route: String
            get() = "trending-now-graph"
        override val label: String
            get() = "Trending now"
    }

    object TrendingNowScreen : TrendingGraphDestinations() {
        override val route: String
            get() = "trending-now-screen"
        override val label: String
            get() = "Trending now"
    }

}

fun NavGraphBuilder.trendingGraph(navController: NavController) {
    navigation(
        startDestination = TrendingGraphDestinations.TrendingNowScreen.route,
        route = TrendingGraphDestinations.TrendingNowGraph.route
    ) {
        composable(route = TrendingGraphDestinations.TrendingNowScreen.route) {
            TrendingRoute(navController)
        }
    }
}