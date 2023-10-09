package com.programmerofpersia.trends.common.navigation

import androidx.compose.ui.graphics.vector.ImageVector


/* todo: change label to @StringRes */
interface TrNavGraph {
    val route: String
    val label: String
    val icon: ImageVector
}
