package com.programmerofpersia.trends.data.domain.model.explore

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SearchDateInfo(

    /*
    * Send inside API request.
    * */
    val id: String,

    /*
    * Display in the UI.
    * */
    val name: String,

    val backend: String
) {


    companion object {

        private const val RAW_DATES =
            "[{\"backend\":\"CM\",\"id\":\"now 1-H\",\"name\":\"Past hour\"},{\"backend\":\"CM\",\"id\":\"now 4-H\",\"name\":\"Past 4 hours\"},{\"backend\":\"CM\",\"id\":\"now 1-d\",\"name\":\"Past day\"},{\"backend\":\"CM\",\"id\":\"now 7-d\",\"name\":\"Past 7 days\"},{\"backend\":\"IZG\",\"id\":\"today 1-m\",\"name\":\"Past 30 days\"},{\"backend\":\"IZG\",\"id\":\"today 3-m\",\"name\":\"Past 90 days\"},{\"backend\":\"IZG\",\"id\":\"today 12-m\",\"name\":\"Past 12 months\"},{\"backend\":\"IZG\",\"id\":\"today 5-y\",\"name\":\"Past 5 years\"},{\"backend\":\"IZG\",\"id\":\"all_2008\",\"name\":\"2008 - present\"},{\"backend\":\"IZG\",\"id\":\"all\",\"name\":\"2004 - present\"}]"

        fun getDateList() = Json.decodeFromString<List<SearchDateInfo>>(RAW_DATES)

    }

}
