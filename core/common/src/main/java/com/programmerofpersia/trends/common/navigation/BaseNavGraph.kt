package com.programmerofpersia.trends.common.navigation


/* todo: change label to @StringRes */
abstract class BaseNavGraph(val route: String, val label: String) {

}

/* todo: change label to @StringRes */
interface TrNavGraph {
    val route: String
    val label: String
}
