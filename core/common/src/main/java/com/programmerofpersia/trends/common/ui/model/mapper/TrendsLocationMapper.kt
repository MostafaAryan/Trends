package com.programmerofpersia.trends.common.ui.model.mapper

import com.programmerofpersia.trends.common.ui.TrItemPickerItem
import com.programmerofpersia.trends.data.domain.TrendsLocation


fun List<TrendsLocation>.toItemPickerItemList(): List<TrItemPickerItem> = map { trendsLocation ->
    TrItemPickerItem(trendsLocation.id, trendsLocation.name)
}