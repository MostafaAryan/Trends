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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


data class TrItemPickerItem(val id: String, val title: String)

@Composable
fun TrItemPickerAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    itemList: List<TrItemPickerItem>,
    onConfirmButtonClicked: (TrItemPickerItem) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        // todo : change default value (should be provided)
        val selectedItem = remember { mutableStateOf(itemList[0]) }

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
                        .verticalScroll(rememberScrollState())
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