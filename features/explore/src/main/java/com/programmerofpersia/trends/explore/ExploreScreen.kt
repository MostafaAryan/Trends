package com.programmerofpersia.trends.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ExploreRoute(
    navController: NavController,
    viewModel: ExploreViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel.loadGeoList()
        viewModel.loadCategoryList()
    }

    ExploreScreen(navController = navController)
}

@Composable
private fun ExploreScreen(navController: NavController) {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 50.dp)
    ) {
        Text(text = "Explore")
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                /*navController.navigate(Screen.TrendingNowGraph.route) {
                    popUpTo(Screen.ExploreGraph.route) {
                        inclusive = true
                    }
                }*/
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Display Trending Now Screen")
        }
    }

}