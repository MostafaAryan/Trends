package com.programmerofpersia.trends.common.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun ClickableChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        shadowElevation = 5.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier.padding(all = 8.dp),
                fontSize = 10.sp /* todo */
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClickableChipPreview() {
    ClickableChip("text", {})
}