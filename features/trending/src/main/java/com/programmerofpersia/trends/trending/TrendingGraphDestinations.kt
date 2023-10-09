package com.programmerofpersia.trends.trending

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
import com.programmerofpersia.trends.common.ui.icon.TrLanguage
import com.programmerofpersia.trends.common.ui.icon.TrTrendingUp
import kotlinx.coroutines.flow.SharedFlow

sealed class TrendingGraphDestinations() : TrNavGraph {

    object TrendingNowGraph : TrendingGraphDestinations() {
        override val route: String
            get() = "trending-now-graph"
        override val label: String
            get() = "Trending now"
        override val icon: ImageVector
            get() = Icons.Outlined.TrTrendingUp
    }

    object TrendingNowScreen : TrendingGraphDestinations(), TrNavScreen {
        override val route: String
            get() = "trending-now-screen"
        override val label: String
            get() = "Trending now"
        override val icon: ImageVector
            get() = Icons.Outlined.TrTrendingUp /* todo : screen entity does not need an icon. */
        override val topAppBarState: TrTopAppBarState
            get() = TrTopAppBarState(
                title = "Daily search trends",
                endIcon = Icons.Outlined.TrLanguage
            )
    }

}

fun NavGraphBuilder.trendingGraph(
    navController: NavController,
    onTopAppBarAction: SharedFlow<TrTopAppBarActions>,
    setTopAppBarState: (TrTopAppBarState) -> Unit,
) {
    navigation(
        startDestination = TrendingGraphDestinations.TrendingNowScreen.route,
        route = TrendingGraphDestinations.TrendingNowGraph.route
    ) {

        composable(route = TrendingGraphDestinations.TrendingNowScreen.route) {
            setTopAppBarState(TrendingGraphDestinations.TrendingNowScreen.topAppBarState)
            TrendingRoute(
                navController = navController,
                onTopAppBarAction = onTopAppBarAction,
            )
        }
    }
}