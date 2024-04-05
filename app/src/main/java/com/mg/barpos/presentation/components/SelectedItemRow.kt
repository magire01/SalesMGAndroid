package com.mg.barpos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectedItemRow(
    itemName: String,
    itemPrice: String,
    selectedSides: Array<String>,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = itemName)
        }

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = itemPrice)
        }

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                description = "Remove Item",
                imageVector = Icons.Rounded.Close,
                tint = Color.Red
            ) {
                onClick()
            }
        }

    }
    Row (
        modifier = Modifier
            .padding(4.dp)
    ) {
        if (selectedSides.isNotEmpty()) {
            for (side in selectedSides) {
                Column (
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    Text("+$side")
                }
            }
        }
    }

}
