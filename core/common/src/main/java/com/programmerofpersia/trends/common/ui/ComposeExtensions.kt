package com.programmerofpersia.trends.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow


@Composable
fun <T> SharedFlow<T>.collectAsEffect(block: (T) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        collect { block(it) }
    }
}