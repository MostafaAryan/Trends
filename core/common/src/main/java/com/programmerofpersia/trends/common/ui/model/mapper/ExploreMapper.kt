package com.programmerofpersia.trends.common.ui.model.mapper

import com.programmerofpersia.trends.common.ui.FilterDialogItem
import com.programmerofpersia.trends.data.domain.model.explore.BaseFilterInfo
import com.programmerofpersia.trends.data.domain.model.explore.BaseFilterInfoImpl
import com.programmerofpersia.trends.data.domain.model.explore.CategoryInfo
import com.programmerofpersia.trends.data.domain.model.explore.GeoInfo
import com.programmerofpersia.trends.data.domain.model.explore.SearchDateInfo
import com.programmerofpersia.trends.data.domain.model.explore.SearchTypeInfo


fun GeoInfo.toFilterDialogItem(): FilterDialogItem = FilterDialogItem(
    id = id,
    title = name,
    children = children.map { it.toFilterDialogItem() }
)

fun CategoryInfo.toFilterDialogItem(): FilterDialogItem = FilterDialogItem(
    id = id,
    title = name,
    children = children.map { it.toFilterDialogItem() }
)

fun SearchTypeInfo.Companion.toFilterDialogItem(): FilterDialogItem {
    return getAllSearchTypesAsList().run {
        FilterDialogItem(
            id = get(0).id,
            title = get(0).name,
            children = drop(1).map {
                FilterDialogItem(
                    id = it.id,
                    title = it.name,
                    children = listOf()
                )
            }
        )
    }
}

fun SearchDateInfo.Companion.toFilterDialogItem(): FilterDialogItem {
    val dateList = getDateList().toMutableList()

    /*
    * In order to select the "12 Month" as default, index 6 has been selected as parent, and all
    * other items are its children.
    *
    * TODO : Improve this implementation (there should be two types of item collection in filterDialog,
    *  First type being nested for geo and category, second type being flat for searchType and searchDate).
    * */
    val defaultDate = dateList.removeAt(6)

    return FilterDialogItem(
        id = defaultDate.id,
        title = defaultDate.name,
        children = dateList.map {
            FilterDialogItem(
                id = it.id,
                title = it.name,
                children = listOf()
            )
        }
    )
}

fun Map<String, FilterDialogItem>.toBaseFilterMap() = map {
    (it.key to BaseFilterInfoImpl(it.value.id, it.value.title))
}.toMap()

fun Map<String, BaseFilterInfo>.fromBaseFilterMap() = map {
    (it.key to FilterDialogItem(id = it.value.id, title = it.value.name, children = listOf()))
}.toMap()