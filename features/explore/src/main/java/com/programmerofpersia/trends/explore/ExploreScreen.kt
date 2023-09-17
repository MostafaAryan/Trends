package com.programmerofpersia.trends.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.programmerofpersia.trends.common.ui.ClickableChipGroup
import com.programmerofpersia.trends.common.ui.CollectAsEffect
import com.programmerofpersia.trends.common.ui.FilterDialog
import com.programmerofpersia.trends.common.ui.FilterDialogItem
import com.programmerofpersia.trends.common.ui.TrTopAppBarActions
import com.programmerofpersia.trends.common.ui.model.mapper.toFilterDialogItem
import com.programmerofpersia.trends.common.ui.theme.spacing
import com.programmerofpersia.trends.data.domain.model.explore.SearchDateInfo
import com.programmerofpersia.trends.data.domain.model.explore.SearchTypeInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordQueryInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordTopicInfo
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ExploreRoute(
    navController: NavController,
    viewModel: ExploreViewModel = hiltViewModel(),
    onTopAppBarAction: SharedFlow<TrTopAppBarActions>,
) {

    LaunchedEffect(key1 = true) {
        viewModel.loadGeoList()
        viewModel.loadCategoryList()
        viewModel.loadSearches()

        viewModel.retrieveSelectedFilters()
    }

    val state by viewModel.state.collectAsState()
    ExploreScreen(
        navController = navController,
        state = state,
        onTopAppBarAction,
        viewModel::storeSelectedFilters,
        viewModel.selectedFilters.collectAsState()
    )
}

@Composable
private fun ExploreScreen(
    navController: NavController,
    state: ExploreState,
    onTopAppBarAction: SharedFlow<TrTopAppBarActions>,
    storeSelectedFilters: (Map<String, FilterDialogItem>) -> Unit,
    selectedFiltersMap: State<Map<String, FilterDialogItem>>
) {

    /*val selectedFiltersMap = remember { mutableStateMapOf<String, FilterDialogItem>() }
    selectedFilters.CollectAsEffect {
        println("Trending-explore: CollectAsEffect: ${it}")

        selectedFiltersMap.clear()
        selectedFiltersMap.putAll(it)
    }*/

    var showFilterDialog by remember { mutableStateOf(false) }
    onTopAppBarAction.CollectAsEffect {
        when (it) {
            TrTopAppBarActions.EndIconClicked -> showFilterDialog = true
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // State - Success
        if (state.areAllScreenDataAvailable()) {

            val lazyListState = rememberLazyListState()

            Column(modifier = Modifier.fillMaxSize()) {

                var displayChipsContainer by rememberSaveable { mutableStateOf(false) }
                val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
                displayChipsContainer = firstVisibleItemIndex == 0

                // Chips & Search Container
                AnimatedVisibility(visible = displayChipsContainer) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = MaterialTheme.spacing.grid_1)
                        /*.offset(y = with(LocalDensity.current) { lazyListState.firstVisibleItemScrollOffset.toDp() })*/
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            //
                            ClickableChipGroup(
                                chipList = getSortedSelectedFiltersList(
                                    state,
                                    selectedFiltersMap.value
                                ),
                                onChipClick = { index ->
                                    /* todo : Go to specific menu associated with the clicked chip. */
                                    showFilterDialog = true
                                },
                                modifier = Modifier.padding(bottom = MaterialTheme.spacing.grid_2)
                            )

                        }
                    }
                }

                var selectedTabIndex by remember { mutableStateOf(0) }
                val tabList = listOf(
                    "Search topics",
                    "Search queries"
                ) /* todo improve + string resource */

                // Tabs bar
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabList.forEachIndexed { index, tabTitle ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = tabTitle) }
                        )
                    }
                }

                // List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, false),
                    state = lazyListState
                ) {
                    when (selectedTabIndex) {
                        0 -> searchedTopicsItems(state.searchedTopicsList!!)
                        1 -> searchedQueriesItems(state.searchedQueriesList!!)
                    }
                }
            }


            if (showFilterDialog) {
                FilterDialog(
                    itemMap = generateAlertDialogItemMap(state),
                    previousSelectionMap = selectedFiltersMap.value.toMutableMap(),
                    onDismissRequest = { showFilterDialog = false },
                    onConfirmButtonClicked = {
                        // Persist data in memory for future access:
                        storeSelectedFilters(it)

                        showFilterDialog = false
                    }
                )
            }
        }

        // State - Loading
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        // State - Error (todo implement error screen)
        if (state.error != null) {
            Text(text = state.error)
        }
    }

}

private fun LazyListScope.searchedTopicsItems(searchedTopicsList: List<KeywordTopicInfo>) {
    itemsIndexed(items = searchedTopicsList) { index, topicItem ->
        KeywordCard(title = topicItem.topic.title, formattedValue = topicItem.formattedValue)
    }
}

private fun LazyListScope.searchedQueriesItems(searchedQueriesList: List<KeywordQueryInfo>) {
    itemsIndexed(items = searchedQueriesList) { index, queryItem ->
        KeywordCard(title = queryItem.query, formattedValue = queryItem.formattedValue)
    }

}

@Composable
private fun KeywordCard(title: String, formattedValue: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = MaterialTheme.spacing.grid_2),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(all = MaterialTheme.spacing.grid_1)
        ) {
            val (keywordTitle, keywordFormattedValue) = createRefs()

            val mediumSpacing = MaterialTheme.spacing.grid_2

            Text(
                modifier = Modifier
                    .padding(all = MaterialTheme.spacing.grid_1)
                    .constrainAs(keywordTitle) {
                        start.linkTo(parent.start)
                        end.linkTo(keywordFormattedValue.start, margin = mediumSpacing)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
                text = title
            )

            Text(
                modifier = Modifier
                    .padding(all = MaterialTheme.spacing.grid_1)
                    .constrainAs(keywordFormattedValue) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                text = formattedValue
            )

        }
    }

}

private fun generateAlertDialogItemMap(state: ExploreState) = linkedMapOf<String, FilterDialogItem>(
    state.geoList!!.name to state.geoList.toFilterDialogItem(),
    SearchDateInfo::class.simpleName!! to SearchDateInfo.Companion.toFilterDialogItem(),
    state.categoryList!!.name to state.categoryList.toFilterDialogItem(),
    SearchTypeInfo::class.simpleName!! to SearchTypeInfo.Companion.toFilterDialogItem()
)

private fun getSortedSelectedFiltersList(
    state: ExploreState,
    selectedFiltersMap: Map<String, FilterDialogItem>
): List<FilterDialogItem> {
    val selectedFiltersList = mutableListOf<FilterDialogItem>()
    generateAlertDialogItemMap(state).forEach { (key, filterDialogItem) ->
        selectedFiltersMap[key]?.let { value -> selectedFiltersList.add(value) }
    }
    return selectedFiltersList
}


/* todo remove : */
@Composable
private fun OldExploreScreen(
    navController: NavController,
    state: ExploreState
) {

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