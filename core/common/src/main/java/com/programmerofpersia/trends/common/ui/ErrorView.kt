package com.programmerofpersia.trends.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.programmerofpersia.trends.common.ui.icon.TrErrorOutline
import com.programmerofpersia.trends.common.ui.theme.spacing


@Composable
fun ErrorView(
    message: String,
    icon: ImageVector = Icons.Outlined.TrErrorOutline
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(all = MaterialTheme.spacing.grid_2),
            tint = Color.DarkGray
        )

        Text(
            text = message,
            color = Color.DarkGray,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.grid_2)
        )

    }

}