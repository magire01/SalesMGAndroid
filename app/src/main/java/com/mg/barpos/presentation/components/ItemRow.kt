package com.mg.barpos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ItemRow(
    itemName: String,
    itemPrice: String,
    selectedSides: Array<String>
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
//            .height(30.dp)
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
    }
    Row (
        modifier = Modifier
            .padding(4.dp)
    ) {
        print(selectedSides)
        if (selectedSides != emptyArray<String>()) {
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
