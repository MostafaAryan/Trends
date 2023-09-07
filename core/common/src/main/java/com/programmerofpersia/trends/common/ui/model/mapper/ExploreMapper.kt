package com.programmerofpersia.trends.common.ui.model.mapper

import com.programmerofpersia.trends.common.ui.FilterDialogItem
import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo


fun GeoInfo.toFilterDialogItem(): FilterDialogItem = FilterDialogItem(
    id = id,
    title = name,
    children = children.map { it.toFilterDialogItem() }
)

fun CategoryInfo.toFilterDialogItem(): FilterDialogItem = FilterDialogItem(
    id = id.toString(),
    title = name,
    children = children.map { it.toFilterDialogItem() }
)