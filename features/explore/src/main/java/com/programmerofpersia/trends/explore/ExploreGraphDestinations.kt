package com.programmerofpersia.trends.explore

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.programmerofpersia.trends.common.navigation.TNavGraph

sealed class ExploreGraphDestinations() : TNavGraph {

    object ExploreGraph : ExploreGraphDestinations() {
        override val route: String
            get() = "explore-graph"
        override val label: String
            get() = "Explore"
    }

    object ExploreScreen : ExploreGraphDestinations() {
        override val route: String
            get() = "explore-screen"
        override val label: String
            get() = "Explore"
    }
}

fun NavGraphBuilder.exploreGraph(navController: NavController) {
    navigation(
        startDestination = ExploreGraphDestinations.ExploreScreen.route,
        route = ExploreGraphDestinations.ExploreGraph.route
    ) {
        composable(route = ExploreGraphDestinations.ExploreScreen.route) {
            ExploreScreen(navController = navController)
        }
    }
}