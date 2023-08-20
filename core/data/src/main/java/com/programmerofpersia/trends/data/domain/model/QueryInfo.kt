package com.programmerofpersia.trends.data.domain.model

import com.programmerofpersia.trends.data.domain.ui.ClickableChipMediator

data class QueryInfo(
    val query: String,
    val exploreLink: String,
) : ClickableChipMediator {

    override fun getClickableChipText() = query

    override fun getClickableChipKey() = exploreLink
}
