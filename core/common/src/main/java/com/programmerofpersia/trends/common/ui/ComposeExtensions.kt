package com.programmerofpersia.trends.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.SharedFlow


@Composable
fun <T> SharedFlow<T>.CollectAsEffect(block: (T) -> Unit) {
    LaunchedEffect(key1 = Unit) {
        collect { block(it) }
    }
}

fun Modifier.conditional(condition: Boolean, block: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(block(this))
    } else this
}