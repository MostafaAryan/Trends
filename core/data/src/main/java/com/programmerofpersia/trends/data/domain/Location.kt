package com.programmerofpersia.trends.data.domain

sealed class Location(val geo : String) {

    object US : Location("US")

    // todo add other locations

}
