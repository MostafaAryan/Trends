package com.programmerofpersia.trends.trending.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.programmerofpersia.trends.common.ui.ClickableChipGroup
import com.programmerofpersia.trends.common.ui.CollectAsEffect
import com.programmerofpersia.trends.common.ui.TrItemPickerAlertDialog
import com.programmerofpersia.trends.common.ui.TrProgressIndicator
import com.programmerofpersia.trends.common.ui.TrTopAppBarActions
import com.programmerofpersia.trends.common.ui.model.mapper.toItemPickerItem
import com.programmerofpersia.trends.common.ui.model.mapper.toItemPickerItemList
import com.programmerofpersia.trends.common.ui.theme.spacing
import com.programmerofpersia.trends.common.ui.theme.trBodySmall
import com.programmerofpersia.trends.common.ui.theme.trBodyXLarge
import com.programmerofpersia.trends.common.ui.theme.trTitleMedium
import com.programmerofpersia.trends.common.ui.theme.trTitleSmall
import com.programmerofpersia.trends.data.domain.TrendsLocation
import com.programmerofpersia.trends.data.domain.model.TrendingSearchInfo
import com.programmerofpersia.trends.trending.R
import com.programmerofpersia.trends.trending.screenstate.TrendingState
import com.programmerofpersia.trends.trending.viewmodel.TrendingViewModel
import kotlinx.coroutines.flow.SharedFlow


@Composable
fun TrendingRoute(
    navController: NavController,
    viewModel: TrendingViewModel = hiltViewModel(),
    onTopAppBarAction: SharedFlow<TrTopAppBarActions>,
) {

    LaunchedEffect(key1 = true) {
        viewModel.initializeNecessaryCookies()
        viewModel.retrieveSelectedLocation()
        viewModel.handleLocationChanges()
    }

    TrendingScreen(
        navController,
        viewModel.countryList,
        viewModel.selectedCountry,
        viewModel::updateSelectedLocationId,
        viewModel.state,
        onTopAppBarAction
    )

}

@Composable
private fun TrendingScreen(
    navController: NavController,
    countryList: List<TrendsLocation>,
    selectedCountry: TrendsLocation?,
    updateSelectedLocationId: (String) -> Unit,
    state: TrendingState,
    onTopAppBarAction: SharedFlow<TrTopAppBarActions>,
) {
    println("trending-c-log: TrendingScreen recomposition")

    var showCountrySelectionDialog by remember { mutableStateOf(false) }
    onTopAppBarAction.CollectAsEffect {
        when (it) {
            TrTopAppBarActions.EndIconClicked -> showCountrySelectionDialog = true
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        state.dailyTrendsInfo?.let { dailyTrends ->

            var expandedItemKey by rememberSaveable { mutableStateOf("") }

            LazyColumn(content = {

                /* Displays selected country name at the beginning of the list. */
                if (!selectedCountry?.name.isNullOrEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = MaterialTheme.spacing.grid_2,
                                    top = MaterialTheme.spacing.grid_2,
                                    end = MaterialTheme.spacing.grid_2
                                ),
                            text = "${selectedCountry?.name ?: ""}:",
                            style = MaterialTheme.typography.trTitleMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Start,
                        )
                    }
                }

                dailyTrends.trendingSearchesDays.forEach { trendingSearchDay ->

                    /* Displays date. */
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = MaterialTheme.spacing.grid_2,
                                    top = MaterialTheme.spacing.grid_2_5,
                                    end = MaterialTheme.spacing.grid_2,
                                    bottom = MaterialTheme.spacing.grid_1
                                ),
                            text = trendingSearchDay.formattedDate,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Normal
                            ),
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    /* Displays trending search items. */
                    itemsIndexed(
                        items = trendingSearchDay.trendingSearches,
                        key = { index, trendingSearch -> trendingSearch.shareUrl }
                    ) { index, trendingSearch ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(all = MaterialTheme.spacing.grid_2)
                                .clickable {
                                    expandedItemKey =
                                        if (expandedItemKey == trendingSearch.shareUrl) "" else trendingSearch.shareUrl
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .animateContentSize()

                            ) {
                                ItemCardVisibleContent(trendingSearch)

                                AnimatedVisibility(trendingSearch.shareUrl == expandedItemKey) {
                                    ItemCardExpandableContent(trendingSearch)
                                }

                            }
                        }
                    }

                }
            })

        }
        if (state.isLoading) {
            TrProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (showCountrySelectionDialog) {
            val context = LocalContext.current
            TrItemPickerAlertDialog(
                onDismissRequest = { showCountrySelectionDialog = false },
                title = stringResource(R.string.title_select),
                itemList = countryList.toItemPickerItemList(),
                initialSelectedItem = selectedCountry?.toItemPickerItem(),
                onConfirmButtonClicked = { selectedItem ->
                    updateSelectedLocationId(selectedItem.id)
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_selected, selectedItem.title),
                        Toast.LENGTH_SHORT
                    ).show()
                    showCountrySelectionDialog = false
                }
            )
        }

    }

}

@Composable
private fun ItemCardVisibleContent(
    trendingSearch: TrendingSearchInfo
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = MaterialTheme.spacing.grid_2)
    ) {
        val (title, traffic, imageBox) = createRefs()
        createVerticalChain(title, traffic, chainStyle = ChainStyle.Packed)

        val mediumSpacing = MaterialTheme.spacing.grid_2_5

        Text(
            text = trendingSearch.title,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                end.linkTo(imageBox.start, margin = mediumSpacing)
                top.linkTo(parent.top)
                bottom.linkTo(traffic.top)
                width = Dimension.fillToConstraints
            },
            style = MaterialTheme.typography.trBodyXLarge
        )
        Text(
            text = stringResource(R.string.label_traffic, trendingSearch.formattedTraffic),
            modifier = Modifier.constrainAs(traffic) {
                start.linkTo(parent.start)
                top.linkTo(title.bottom)
                bottom.linkTo(parent.bottom)
            },
            style = MaterialTheme.typography.trBodySmall
        )
        Card(
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
                .constrainAs(imageBox) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = trendingSearch.image?.url ?: "",
                    contentDescription = trendingSearch.title,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 100f
                            ),
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 1.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 1.dp),
                        text = trendingSearch.image?.source ?: "",
                        color = Color.White,
                        fontSize = 5.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

}

@Composable
private fun ItemCardExpandableContent(trendingSearch: TrendingSearchInfo) {
    var parentSize by remember {
        mutableStateOf(Size.Zero)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = MaterialTheme.spacing.grid_2,
                end = MaterialTheme.spacing.grid_2,
                bottom = MaterialTheme.spacing.grid_2
            )
            .onGloballyPositioned { coordinates ->
                parentSize = coordinates.size.toSize()
            }
    ) {
        val localContext = LocalContext.current

        if (trendingSearch.relatedQueries.isNotEmpty()) {
            Divider(thickness = 1.dp, color = Color.LightGray)
            Text(
                text = stringResource(id = R.string.label_related_queries),
                style = MaterialTheme.typography.trTitleSmall,
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.grid_2)
            )
            ClickableChipGroup(
                chipList = trendingSearch.relatedQueries,
                onChipClick = { index ->
                    /*todo: navigate to explore feature*/
                    /*navController.navigate(Screen.ExploreGraph.route) {
                        popUpTo(Screen.TrendingNowGraph.route) {
                            inclusive = true
                        }
                    }*/
                    Toast.makeText(
                        localContext,
                        trendingSearch.relatedQueries[index].query,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.grid_2)
            )
        }

        if (trendingSearch.articles.isNotEmpty()) {
            Divider(thickness = 1.dp, color = Color.LightGray)
            Text(
                text = stringResource(R.string.label_related_news),
                style = MaterialTheme.typography.trTitleSmall,
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.grid_2)
            )
            LazyRow {
                itemsIndexed(
                    items = trendingSearch.articles,
                    key = { _, article -> article.url }
                ) { index, article ->
                    ArticleItem(article = article, parentSize)
                }
            }
        }

    }

}

