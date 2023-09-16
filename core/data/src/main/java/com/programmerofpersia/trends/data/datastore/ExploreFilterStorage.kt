package com.programmerofpersia.trends.data.datastore

import com.programmerofpersia.trends.data.domain.model.explore.BaseFilterInfo

interface ExploreFilterStorage : LocalStorage<Map<String, BaseFilterInfo>> {


}