package com.mg.barpos.presentation.OrderContainer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mg.barpos.R
import com.mg.barpos.presentation.ItemEvent
import com.mg.barpos.presentation.OrderEvent
import com.mg.barpos.presentation.OrderState
import com.mg.barpos.presentation.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    state: OrderState,
    navController: NavController,
    onEvent: (OrderEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Orders",
                button = null,
                navController = navController
            )
        },
    ) { paddingValues ->
        Row {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                items(state.orders.size) { index ->
                    Row {
                        OutlinedCard(
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            onClick = {
                                state.selectedOrderNumber.value = state.orders[index].orderNumber
                                state.selectedOrderName.value = state.orders[index].orderName
                                state.orderTotal.value = state.orders[index].orderTotal
                                navController.navigate("SavedOrderDetails")
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()


                            ) {
                                Text(
                                    state.orders[index].orderNumber.toString(),
                                    modifier = Modifier.padding(20.dp)
                                )

                                Text(
                                    state.orders[index].orderName,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}
