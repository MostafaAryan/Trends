package com.programmerofpersia.trends.data.domain.model.explore

data class GeoInfo(
    val children: List<GeoInfo>,
    override val name: String,
    override val id: String,
) : BaseFilterInfo {

    companion object {
        /* todo improve implementation to avoid not-null symbol. */
        val key = GeoInfo::class.simpleName!!
    }

}
