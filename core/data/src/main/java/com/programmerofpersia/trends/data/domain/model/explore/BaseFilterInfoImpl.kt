package com.programmerofpersia.trends.data.domain.model.explore

import kotlinx.serialization.Serializable

@Serializable
class BaseFilterInfoImpl(

    override val id: String,

    override val name: String

) : BaseFilterInfo