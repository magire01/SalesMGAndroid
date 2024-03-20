package com.mg.barpos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mg.barpos.data.Item
import com.mg.barpos.data.MenuItem
import com.mg.barpos.presentation.OrderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCard(
    itemName: String,
    itemPrice: String,
    onClick: () -> Unit,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .padding(8.dp)
//                                .size(width = 400.dp, height = 60.dp),
            .height(60.dp)
            .fillMaxWidth(),

        onClick = {
            onClick()
        }
    ) {
        Row (
            modifier = Modifier
//                                    .size(width = 240.dp, height = 100.dp)
                .fillMaxWidth()
                .padding(8.dp)
                .height(100.dp)
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
                Text(text = "$" + itemPrice)
            }
        }
    }
}