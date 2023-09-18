package com.programmerofpersia.trends.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.programmerofpersia.trends.common.ui.theme.spacing
import com.programmerofpersia.trends.common.ui.theme.trLabelXLarge
import com.programmerofpersia.trends.common.ui.theme.trTitleLarge
import com.programmerofpersia.trends.data.domain.ui.ClickableChipMediator


data class FilterDialogItem(
    val id: String,
    val title: String,
    val icon: String? = null,
    val children: List<FilterDialogItem>
) : ClickableChipMediator {

    override fun getClickableChipText() = title

    override fun getClickableChipKey() = "${id}-${title}"

}


@Composable
fun FilterDialog(
    itemMap: LinkedHashMap<String, FilterDialogItem>,
    previousSelectionMap: MutableMap<String, FilterDialogItem>,
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: (Map<String, FilterDialogItem>) -> Unit
) {

    /**
     * Indicates which filter-type is being displayed currently (e.g., location, category, etc.).
     * When it is 'null', parent menu is being displayed.
     **/
    val levelListKey = remember { mutableStateOf<String?>(null) }

    /**
     * Keeps track of how deep are we currently inside a specific filter-type.
     * The last item in the list is the parent of the current menu being displayed and the current
     * menu items are its children.
     * When list is empty, we are at the parent menu.
     *
     * For example:
     * - When user clicks on Canada, the FilterDialogItem associated with it is added to the list
     * and we are now one level deep, displaying Canada.children items.
     * - Then user clicks, Ontario, and the FilterDialogItem associated with it is added to the list,
     * now we are two levels deep, displaying Ontario.children.
     *
     **/
    val levelListState = remember { mutableStateListOf<FilterDialogItem>() }

    val selectionMap = remember { mutableStateMapOf<String, FilterDialogItem>() }


    /**
     * Populates local selection-state with the previously selected items.
     **/
    LaunchedEffect(key1 = Unit) {
        selectionMap.clear()
        selectionMap.putAll(previousSelectionMap)
    }


    //
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.background)
                .padding(all = MaterialTheme.spacing.grid_2)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // Dialog title
                Text(
                    modifier = Modifier.padding(all = MaterialTheme.spacing.grid_2),
                    text = "Filter", /* todo string resource */
                    style = MaterialTheme.typography.trTitleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )

                // Item list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = MaterialTheme.spacing.grid_1)
                        .weight(1f, false)
                ) {
                    ItemList(
                        itemMap,
                        selectionMap,
                        levelListState,
                        levelListKey
                    )
                }

                // Submit button
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.grid_1),
                    onClick = { onConfirmButtonClicked(selectionMap) }
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Apply" /* todo change and string resource */,
                        style = MaterialTheme.typography.trLabelXLarge
                    )
                }

            }
        }

    }
}

@Composable
private fun ItemList(
    itemMap: LinkedHashMap<String, FilterDialogItem>,
    selectionMap: SnapshotStateMap<String, FilterDialogItem>,
    levelListState: SnapshotStateList<FilterDialogItem>,
    levelListKey: MutableState<String?>
) {
    if (levelListState.isEmpty()) {
        ParentLevel(
            itemMap = itemMap,
            selectionMap = selectionMap,
            onItemSelected = { key, selectedItem ->
                if (selectedItem.children.isNotEmpty()) {
                    levelListState.add(selectedItem)
                    levelListKey.value = key
                }
            }
        )
    } else {
        SubLevel(
            levelListState = levelListState,
            onItemSelected = { selectedSubItem ->
                if (selectedSubItem.children.isEmpty()) {
                    // select
                    levelListKey.value?.let {
                        selectionMap[it] = selectedSubItem

                        // Reset dialog
                        levelListState.clear()
                        levelListKey.value = null
                    }
                } else {
                    levelListState.add(selectedSubItem)
                }
            },
            onBackArrowClick = {
                levelListState.removeLast()
                if (levelListState.isEmpty()) levelListKey.value = null
            }
        )
    }

}


@Composable
private fun ParentLevel(
    itemMap: LinkedHashMap<String, FilterDialogItem>,
    selectionMap: SnapshotStateMap<String, FilterDialogItem>,
    onItemSelected: (key: String, FilterDialogItem) -> Unit
) {
    itemMap.forEach { (key, item) ->
        Card(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.grid_1),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = MaterialTheme.shapes.small,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = MaterialTheme.spacing.grid_1)
                    .clickable { onItemSelected(key, item) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, false)
                        .padding(all = MaterialTheme.spacing.grid_1),
                    // If there is an item selected in that key, display its title,
                    // otherwise, display the default parent title:
                    text = selectionMap[key]?.title ?: item.title,
                    style = MaterialTheme.typography.trLabelXLarge
                )

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow."
                )
            }
        }
    }
}

@Composable
private fun SubLevel(
    levelListState: List<FilterDialogItem>,
    onItemSelected: (FilterDialogItem) -> Unit,
    onBackArrowClick: (() -> Unit)
) {
    if (levelListState.isNotEmpty()) {
        LazyColumn {

            val parent = levelListState.lastOrNull()
            if (parent != null) {
                item {
                    // Parent item should not have any children in order to be selected when clicked.
                    SubItemCard(
                        subItem = FilterDialogItem(
                            id = parent.id,
                            title = parent.title,
                            children = listOf()
                        ),
                        onSubItemSelected = onItemSelected,
                        onBackArrowClick = onBackArrowClick
                    )
                }

                itemsIndexed(items = parent.children) { index, item ->
                    SubItemCard(subItem = item, onSubItemSelected = onItemSelected)
                }
            }
        }
    }
}

@Composable
private fun SubItemCard(
    subItem: FilterDialogItem,
    onSubItemSelected: (FilterDialogItem) -> Unit,
    onBackArrowClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.padding(vertical = MaterialTheme.spacing.grid_1),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = MaterialTheme.spacing.grid_1),
            verticalAlignment = Alignment.CenterVertically
        ) {

            onBackArrowClick?.apply {
                Icon(
                    modifier = Modifier.clickable {
                        invoke()
                    },
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back arrow."
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
                    .padding(all = MaterialTheme.spacing.grid_1)
                    .clickable {
                        onSubItemSelected(
                            // Select the item itself. Do not display its children.
                            FilterDialogItem(
                                id = subItem.id,
                                title = subItem.title,
                                children = listOf()
                            )
                        )
                    },
                text = subItem.title,
                style = MaterialTheme.typography.trLabelXLarge
            )

            if (subItem.children.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable {
                        // Display item's children.
                        onSubItemSelected(subItem)
                    },
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Arrow."
                )
            }

        }
    }

}
