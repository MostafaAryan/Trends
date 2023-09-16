package com.programmerofpersia.trends.data.domain.model.explore

import kotlinx.serialization.Serializable

/**
 * Both [BaseFilterInfo] and [BaseFilterInfoImpl] can be used in
 * [ExploreFilterDataStoreImpl], however, when using [BaseFilterInfo]
 * it should be a sealed interface and not a regular interface.
 **/
@Serializable
sealed interface BaseFilterInfo {

    val id: String

    val name: String

}