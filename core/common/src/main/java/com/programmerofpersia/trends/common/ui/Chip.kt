package com.programmerofpersia.trends.common.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.programmerofpersia.trends.common.ui.theme.spacing
import com.programmerofpersia.trends.common.ui.theme.trLabelMedium
import com.programmerofpersia.trends.data.domain.ui.ClickableChipMediator


@Composable
fun <T : ClickableChipMediator> ClickableChipGroup(
    chipList: List<T>,
    onChipClick: (Int) -> Unit,
    modifier: Modifier
) {
    LazyRow(
        modifier = modifier
    ) {
        itemsIndexed(
            items = chipList,
            key = { _, item -> item.getClickableChipKey() }
        ) { index, item ->
            ClickableChip(item.getClickableChipText(), onClick = { onChipClick(index) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = { onClick() },
        modifier = Modifier.padding(end = 8.dp),
        shadowElevation = 1.dp,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row {
            Text(
                text = text,
                style = MaterialTheme.typography.trLabelMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(
                    vertical = MaterialTheme.spacing.grid_1,
                    horizontal = MaterialTheme.spacing.grid_2
                ),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClickableChipPreview() {
    ClickableChip("text", {})
}