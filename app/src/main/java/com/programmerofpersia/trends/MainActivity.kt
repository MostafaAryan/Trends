package com.programmerofpersia.trends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.programmerofpersia.trends.common.ui.TrTopAppBar
import com.programmerofpersia.trends.common.ui.TrTopAppBarActions
import com.programmerofpersia.trends.common.ui.TrTopAppBarState
import com.programmerofpersia.trends.explore.ExploreGraphDestinations
import com.programmerofpersia.trends.explore.exploreGraph
import com.programmerofpersia.trends.trending.TrendingGraphDestinations
import com.programmerofpersia.trends.trending.trendingGraph
import com.programmerofpersia.trends.ui.theme.TrendsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrendsTheme {
                // A surface container using the 'background' color from the theme
                /*Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }*/

                Navigation()
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navBarItems =
        listOf(TrendingGraphDestinations.TrendingNowGraph, ExploreGraphDestinations.ExploreGraph)

    var topAppBarState by remember { mutableStateOf(TrTopAppBarState.Hidden) }
    val onTopAppBarAction = MutableSharedFlow<TrTopAppBarActions>()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedVisibility(topAppBarState.isVisible) {
                TrTopAppBar(
                    state = topAppBarState,
                    onEndIconClick = {
                        coroutineScope.launch { onTopAppBarAction.emit(TrTopAppBarActions.EndIconClicked) }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navBarItems.forEach { navBarItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navBarItem.route } == true,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = null
                            )
                        },
                        label = { Text(text = navBarItem.label) },
                        onClick = {
                            navController.navigate(navBarItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    // inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TrendingGraphDestinations.TrendingNowGraph.route,
            Modifier.padding(innerPadding)
        ) {
            trendingGraph(navController, onTopAppBarAction) { topAppBarState = it }
            exploreGraph(navController)
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
/*@Preview(
    name = "Dark mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)*/

@Composable
fun GreetingPreview() {
    TrendsTheme {
        // Greeting("Android")
        Navigation()
    }
}