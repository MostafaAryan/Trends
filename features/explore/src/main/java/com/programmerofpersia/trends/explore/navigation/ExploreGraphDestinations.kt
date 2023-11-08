package com.programmerofpersia.trends.explore.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.programmerofpersia.trends.common.navigation.TrNavGraph
import com.programmerofpersia.trends.common.navigation.TrNavScreen
import com.programmerofpersia.trends.common.ui.TrTopAppBarActions
import com.programmerofpersia.trends.common.ui.TrTopAppBarState
import com.programmerofpersia.trends.common.ui.icon.TrFilterAlt
import com.programmerofpersia.trends.common.ui.icon.TrTravelExplore
import com.programmerofpersia.trends.explore.screen.ExploreRoute
import kotlinx.coroutines.flow.SharedFlow

sealed class ExploreGraphDestinations() : TrNavGraph {

    object ExploreGraph : ExploreGraphDestinations() {
        override val route: String
            get() = "explore-graph"
        override val label: String
            get() = "Explore"
        override val icon: ImageVector
            get() = Icons.Outlined.TrTravelExplore
    }

    object ExploreScreen : ExploreGraphDestinations(), TrNavScreen {
        override val route: String
            get() = "explore-screen"
        override val label: String
            get() = "Explore"
        override val icon: ImageVector
            get() = Icons.Outlined.TrTravelExplore /* todo: screen entity does not need an icon. */
        override val topAppBarState: TrTopAppBarState
            get() = TrTopAppBarState(title = "Explore", endIcon = Icons.Outlined.TrFilterAlt)
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