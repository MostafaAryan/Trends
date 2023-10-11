package com.programmerofpersia.trends.common.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.programmerofpersia.trends.common.ui.theme.spacing


@Composable
fun TrProgressIndicator(modifier: Modifier = Modifier) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        CircularProgressIndicator(Modifier.padding(all = MaterialTheme.spacing.grid_3))
    }

}