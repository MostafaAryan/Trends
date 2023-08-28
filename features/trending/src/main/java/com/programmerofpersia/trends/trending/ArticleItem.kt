package com.programmerofpersia.trends.trending

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.programmerofpersia.trends.common.ui.theme.spacing
import com.programmerofpersia.trends.data.domain.model.ArticleInfo


@Composable
fun ArticleItem(
    article: ArticleInfo,
    parentSize: Size
) {
    Card(
        modifier = Modifier
            .width(width = with(LocalDensity.current) { parentSize.width.toDp() } - 30.dp)
            .height(90.dp)
            .padding(end = MaterialTheme.spacing.grid_2),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (articleTitle, articleSource, articleImage) = createRefs()
            createVerticalChain(articleTitle, articleSource, chainStyle = ChainStyle.Packed)

            val smallSpacing = MaterialTheme.spacing.grid_2

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
                    start.linkTo(articleImage.end, margin = smallSpacing)
                    end.linkTo(parent.end, margin = smallSpacing)
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
                        start.linkTo(articleImage.end, margin = smallSpacing)
                        end.linkTo(parent.end, margin = smallSpacing)
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
