package com.programmerofpersia.trends.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.roundToInt


data class TrItemPickerItem(val id: String, val title: String)

@Composable
fun TrItemPickerAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    itemList: List<TrItemPickerItem>,
    initialSelectedItem: TrItemPickerItem? = null,
    onConfirmButtonClicked: (TrItemPickerItem) -> Unit
) {
    if (itemList.isEmpty()) return

    Dialog(
        onDismissRequest = onDismissRequest
    ) {

        val selectedItem = remember { mutableStateOf(initialSelectedItem ?: itemList[0]) }
        var selectedItemPositionOnScreen by remember { mutableStateOf(0F) }
        val scrollState = rememberScrollState()

        LaunchedEffect(key1 = Unit) {
            scrollState.animateScrollTo(selectedItemPositionOnScreen.roundToInt())
        }

        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.background)
                .padding(all = 10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    modifier = Modifier.padding(all = 10.dp),
                    text = title,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                )

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxWidth()
                        .weight(1f, false)
                ) {
                    itemList.forEach { item ->
                        Row(
                            modifier = Modifier.selectable(
                                selected = selectedItem.value.id == item.id,
                                onClick = { selectedItem.value = item }
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                modifier = Modifier
                                    .conditional(selectedItem.value.id == item.id) {
                                        onGloballyPositioned { coordinates ->
                                            selectedItemPositionOnScreen =
                                                coordinates.positionInRoot().y
                                        }
                                    },
                                selected = selectedItem.value.id == item.id,
                                onClick = { selectedItem.value = item }
                            )
                            Text(
                                modifier = Modifier.padding(all = 5.dp),
                                text = item.title
                            )
                        }
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    onClick = { onConfirmButtonClicked(selectedItem.value) }
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Select",
                        fontSize = 17.sp,
                    )
                }

            }
        }

    }


}