package com.programmerofpersia.trends.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class Spacing(
    val default: Dp = 0.dp,
    val grid_1: Dp = 5.dp,
    val grid_2: Dp = 10.dp,
    val grid_2_5: Dp = 15.dp,
    val grid_3: Dp = 20.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current