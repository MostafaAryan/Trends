package com.programmerofpersia.trends.data.remote.model.mappers

import com.programmerofpersia.trends.data.domain.model.ArticleInfo
import com.programmerofpersia.trends.data.domain.model.DailyTrendsInfo
import com.programmerofpersia.trends.data.domain.model.ImageInfo
import com.programmerofpersia.trends.data.domain.model.QueryInfo
import com.programmerofpersia.trends.data.domain.model.TrendingSearchDayInfo
import com.programmerofpersia.trends.data.domain.model.TrendingSearchInfo
import com.programmerofpersia.trends.data.domain.util.fromHtml
import com.programmerofpersia.trends.data.remote.model.dto.DefaultDailyTrendsDto


fun DefaultDailyTrendsDto.toDailyTrendsInfo(): DailyTrendsInfo {

    return DailyTrendsInfo(
        rssFeedPageUrl,
        trendingSearchesDays.map { trendingSearchDayDto ->

            TrendingSearchDayInfo(
                trendingSearchDayDto.formattedDate,
                trendingSearchDayDto.trendingSearches.map { trendingSearchDto ->

                    trendingSearchDto.run {
                        TrendingSearchInfo(
                            title.query,
                            formattedTraffic,
                            relatedQueries.map {
                                QueryInfo(
                                    it.query,
                                    it.exploreLink
                                )
                            },
                            ImageInfo(
                                image.imageUrl,
                                image.source
                            ),
                            articles.map {
                                it.run {
                                    ArticleInfo(
                                        title.fromHtml(),
                                        timeAgo,
                                        image?.run { ImageInfo(imageUrl, source) },
                                        url,
                                        source,
                                        snippet
                                    )
                                }
                            },
                            shareUrl
                        )

                    }

                }

            )

        }

    )

}