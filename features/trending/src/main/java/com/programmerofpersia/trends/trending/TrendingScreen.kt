package com.programmerofpersia.trends.trending

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalDensity
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
import com.programmerofpersia.trends.data.domain.model.ArticleInfo
import com.programmerofpersia.trends.data.domain.model.TrendingSearchInfo


@Composable
fun TrendingRoute(
    navController: NavController,
    viewModel: TrendingViewModel = hiltViewModel()
) {

    println("trending-c-log: TrendingRoute recomposition")

    LaunchedEffect(key1 = true) {
        viewModel.loadDailyTrends()
        println("trending-c-log: LaunchedEffect recomposition")
    }

    TrendingScreen(
        navController,
        viewModel::loadDailyTrends,
        viewModel.state
    )

}

@Composable
fun TrendingScreen(
    navController: NavController,
    loadDailyTrends: () -> Unit,
    state: TrendingState
) {
    println("trending-c-log: TrendingScreen recomposition")

    Box(modifier = Modifier.fillMaxSize()) {
        state.dailyTrendsInfo?.let { dailyTrends ->

            var expandedItemIndex by rememberSaveable { mutableStateOf(-1) }

            LazyColumn(content = {

                itemsIndexed(
                    items = dailyTrends.trendingSearchesDays[1].trendingSearches,
                    key = { index, trendingSearchInfo -> trendingSearchInfo.shareUrl }
                ) { index, trendingSearch ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(all = 10.dp)
                            .clickable {
                                expandedItemIndex = if (expandedItemIndex == index) -1 else index
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

                            AnimatedVisibility(index == expandedItemIndex) {
                                ItemCardExpandableContent(trendingSearch)
                            }

                        }
                    }
                }
            })

        }
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }

}

@Composable
fun ItemCardVisibleContent(
    trendingSearch: TrendingSearchInfo
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 10.dp)
    ) {
        val (title, traffic, imageBox) = createRefs()
        createVerticalChain(title, traffic, chainStyle = ChainStyle.Packed)

        Text(
            text = trendingSearch.title,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                end.linkTo(imageBox.start, margin = 15.dp)
                top.linkTo(parent.top)
                bottom.linkTo(traffic.top)
                width = Dimension.fillToConstraints
            },
            fontSize = 20.sp,
        )
        Text(
            text = "${trendingSearch.formattedTraffic} searches",
            modifier = Modifier.constrainAs(traffic) {
                start.linkTo(parent.start)
                top.linkTo(title.bottom)
                bottom.linkTo(parent.bottom)
            },
            fontSize = 10.sp,
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
                    model = trendingSearch.image.url,
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
                        text = trendingSearch.image.source,
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
fun ItemCardExpandableContent(trendingSearch: TrendingSearchInfo) {
    var parentSize by remember {
        mutableStateOf(Size.Zero)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .onGloballyPositioned { coordinates ->
                parentSize = coordinates.size.toSize()
            }
    ) {
        val localContext = LocalContext.current

        if (trendingSearch.relatedQueries.isNotEmpty()) {
            Divider(thickness = 1.dp, color = Color.LightGray)
            Text(
                text = "Related Queries",
                fontSize = 15.sp, /* todo */
                modifier = Modifier.padding(vertical = 10.dp)
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
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        if (trendingSearch.articles.isNotEmpty()) {
            Divider(thickness = 1.dp, color = Color.LightGray)
            Text(
                text = "Related News",
                fontSize = 15.sp, /* todo */
                modifier = Modifier.padding(vertical = 10.dp)
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

@Composable
fun ArticleItem(
    article: ArticleInfo,
    parentSize: Size
) {
    Card(
        modifier = Modifier
            .width(width = with(LocalDensity.current) { parentSize.width.toDp() } - 30.dp)
            .height(90.dp)
            .padding(end = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (articleTitle, articleSource, articleImage) = createRefs()
            createVerticalChain(articleTitle, articleSource, chainStyle = ChainStyle.Packed)

            Box(
                modifier = Modifier
                    .height(90.dp)
                    .width(90.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
                    .constrainAs(articleImage) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                AsyncImage(
                    model = article.image?.url ?: "",
                    contentDescription = article.title,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = article.title,
                modifier = Modifier.constrainAs(articleTitle) {
                    start.linkTo(articleImage.end, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(articleSource.top)
                    width = Dimension.fillToConstraints
                },
                fontSize = 13.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )

            Text(
                text = "${article.source} • ${article.timeAgo}",
                modifier = Modifier
                    .padding(top = 2.dp)
                    .constrainAs(articleSource) {
                        start.linkTo(articleImage.end, margin = 10.dp)
                        end.linkTo(parent.end, margin = 10.dp)
                        top.linkTo(articleTitle.bottom)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    },
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}
