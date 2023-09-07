package com.programmerofpersia.trends.explore

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.programmerofpersia.trends.common.navigation.TrNavGraph
import com.programmerofpersia.trends.common.navigation.TrNavScreen
import com.programmerofpersia.trends.common.ui.TrTopAppBarActions
import com.programmerofpersia.trends.common.ui.TrTopAppBarState
import kotlinx.coroutines.flow.SharedFlow

sealed class ExploreGraphDestinations() : TrNavGraph {

    object ExploreGraph : ExploreGraphDestinations() {
        override val route: String
            get() = "explore-graph"
        override val label: String
            get() = "Explore"
    }

    object ExploreScreen : ExploreGraphDestinations(), TrNavScreen {
        override val route: String
            get() = "explore-screen"
        override val label: String
            get() = "Explore"
        override val topAppBarState: TrTopAppBarState
            get() = TrTopAppBarState(title = "Explore", endIcon = "Q")
    }
}

fun NavGraphBuilder.exploreGraph(
    navController: NavController,
    onTopAppBarAction: SharedFlow<TrTopAppBarActions>,
    setTopAppBarState: (TrTopAppBarState) -> Unit,
) {
    navigation(
        startDestination = ExploreGraphDestinations.ExploreScreen.route,
        route = ExploreGraphDestinations.ExploreGraph.route
    ) {
        composable(route = ExploreGraphDestinations.ExploreScreen.route) {
            setTopAppBarState(ExploreGraphDestinations.ExploreScreen.topAppBarState)
            ExploreRoute(
                navController = navController,
                onTopAppBarAction = onTopAppBarAction,
            )
        }
    }
}