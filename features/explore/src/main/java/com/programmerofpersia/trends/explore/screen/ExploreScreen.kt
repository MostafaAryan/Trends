package com.programmerofpersia.trends.explore.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.programmerofpersia.trends.common.ui.ClickableChipGroup
import com.programmerofpersia.trends.common.ui.CollectAsEffect
import com.programmerofpersia.trends.common.ui.ErrorView
import com.programmerofpersia.trends.common.ui.FilterDialog
import com.programmerofpersia.trends.common.ui.FilterDialogItem
import com.programmerofpersia.trends.common.ui.TrProgressIndicator
import com.programmerofpersia.trends.common.ui.TrTextField
import com.programmerofpersia.trends.common.ui.TrTopAppBarActions
import com.programmerofpersia.trends.common.ui.theme.spacing
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordQueryInfo
import com.programmerofpersia.trends.data.domain.model.explore.keyword.KeywordTopicInfo
import com.programmerofpersia.trends.explore.screenstate.ExploreState
import com.programmerofpersia.trends.explore.screenstate.ExploreStateHolder
import com.programmerofpersia.trends.explore.viewmodel.ExploreViewModel
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ExploreRoute(
    navController: NavController,
    viewModel: ExploreViewModel = hiltViewModel(),
    onTopAppBarAction: SharedFlow<TrTopAppBarActions>,
) {

    LaunchedEffect(key1 = true) {
        viewModel.prepareFilters()
    }

    val stateHolder by viewModel.state.collectAsState()
    val searchKeyword by viewModel.searchKeywordState.collectAsState()

    ExploreScreen(
        navController = navController,
        stateHolder = stateHolder,
        onTopAppBarAction,
        viewModel.generateFilterDialogParentMap(stateHolder),
        viewModel::storeSelectedFilters,
        viewModel.selectedFiltersState.collectAsState(),
        searchKeyword,
        viewModel::updateSearchKeyword
    )
}

@Composable
private fun ExploreScreen(
    navController: NavController,
    stateHolder: ExploreStateHolder,
    onTopAppBarAction: SharedFlow<TrTopAppBarActions>,
    filterDialogParentMap: LinkedHashMap<String, FilterDialogItem>,
    storeSelectedFilters: (Map<String, FilterDialogItem>) -> Unit,
    selectedFiltersMap: State<Map<String, FilterDialogItem>>,
    searchKeyword: String,
    updateSearchKeyword: (String) -> Unit
) {

    var showFilterDialog by remember { mutableStateOf(false) }
    onTopAppBarAction.CollectAsEffect {
        when (it) {
            TrTopAppBarActions.EndIconClicked -> showFilterDialog = true
            else -> {}
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        // State - Loading
        if (stateHolder.currentState() is ExploreState.Loading.FullScreen) {
            TrProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // State - Success
        val lazyListState = rememberLazyListState()

        Column(modifier = Modifier.fillMaxSize()) {

            if (stateHolder.atLeastFilterDataIsAvailable()) {

                var displayChipsContainer by rememberSaveable { mutableStateOf(false) }
                val firstVisibleItemIndex by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
                displayChipsContainer = firstVisibleItemIndex == 0

                // Chips & Search Container
                AnimatedVisibility(visible = displayChipsContainer) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                    filterDialogParentMap,
                                    selectedFiltersMap.value
                                ),
                                onChipClick = { index ->
                                    /* todo : Go to specific menu associated with the clicked chip. */
                                    showFilterDialog = true
                                },
                                modifier = Modifier.padding(
                                    start = MaterialTheme.spacing.grid_1,
                                    bottom = MaterialTheme.spacing.grid_1
                                )
                            )

                            //
                            TrTextField(
                                value = searchKeyword,
                                onValueChange = { updateSearchKeyword(it) },
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .padding(top = 0.dp, bottom = 10.dp),
                                textStyle = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = 17.sp
                                ),
                                placeholder = {
                                    Text(
                                        text = "Add a search term",
                                        color = Color.Gray
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Search.",
                                        modifier = Modifier
                                            .padding(start = MaterialTheme.spacing.grid_2_5),
                                        tint = Color.LightGray
                                    )
                                },
                                trailingIcon = {
                                    if (stateHolder.currentState() is ExploreState.Loading.SearchField) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.spacing.grid_2)
                                                .then(Modifier.size(20.dp)),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                    } else if (searchKeyword.isNotEmpty())
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Clear search.",
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.spacing.grid_2_5)
                                                .clickable {
                                                    updateSearchKeyword("")
                                                },
                                            tint = Color.LightGray
                                        )
                                },
                                /*contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                                top = 0.dp,
                                bottom = 0.dp
                            )*/
                            )

                        }
                    }
                }


            }

            // Tabs and List
            if (stateHolder.currentState() is ExploreState.Success.AllScreenDataIsAvailable &&
                (stateHolder.currentState() !is ExploreState.Loading)
            ) {

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
                        0 -> searchedTopicsItems(stateHolder.searchedTopicsList!!)
                        1 -> searchedQueriesItems(stateHolder.searchedQueriesList!!)
                    }
                }

            } else if (stateHolder.currentState() is ExploreState.Error) {
                // State - Error
                ErrorView(
                    message = (stateHolder.currentState() as ExploreState.Error).message ?: ""
                )
            }

        }


        if (stateHolder.atLeastFilterDataIsAvailable() && showFilterDialog) {
            FilterDialog(
                itemMap = filterDialogParentMap,
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

private fun getSortedSelectedFiltersList(
    filterDialogParentMap: LinkedHashMap<String, FilterDialogItem>,
    selectedFiltersMap: Map<String, FilterDialogItem>
): List<FilterDialogItem> {
    val selectedFiltersList = mutableListOf<FilterDialogItem>()
    filterDialogParentMap.forEach { (key, filterDialogItem) ->
        selectedFiltersMap[key]?.let { value -> selectedFiltersList.add(value) }
    }
    return selectedFiltersList
}
