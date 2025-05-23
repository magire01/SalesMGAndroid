package com.mg.barpos.presentation.OrderContainer.MyOrders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mg.barpos.R
import com.mg.barpos.presentation.ItemEvent
import com.mg.barpos.presentation.OrderContainer.MyOrders.MyOrdersState
import com.mg.barpos.presentation.OrderEvent
import com.mg.barpos.presentation.OrderState
import com.mg.barpos.presentation.components.IconButton
import com.mg.barpos.presentation.components.ItemRow
import com.mg.barpos.presentation.components.TopBar
import com.mg.barpos.presentation.components.TotalRow
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SavedOrderDetails(
    state: MyOrdersState,
    navController: NavController,
    onEvent: (ItemEvent) -> Unit,
    onOrderEvent: (OrderEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Order # ${state.selectedOrderNumber.value}",
                navController = navController,
                button = {
                    IconButton(
                        description = "Back",
                        imageVector = Icons.Rounded.ArrowBack
                    ) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { paddingValues ->
        onEvent(ItemEvent.GetItemById(state.selectedOrderNumber.value))
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.orderName.value,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            items(state.items.size) { index ->
                ItemRow(
                    itemName = state.items[index].itemName,
                    itemPrice = state.items[index].itemPrice.toString(),
                    selectedSides = state.items[index].selectedSides
                )
            }
            item {
                TotalRow(
                    orderTotal = state.orderTotal.value
                )
            }

            item {
                Row {
                    Button(onClick = {
                        onOrderEvent(
                            OrderEvent.PrintBluetooth(
                                orderNumber = state.selectedOrderNumber.value,
                                items = state.items
                            )
                        )
                    }) {
                        Text("Print Customer")
                    }

                    Button(onClick = {
                        onOrderEvent(
                            OrderEvent.PrintNetwork(
                                orderNumber = state.selectedOrderNumber.value,
                                items = state.items
                            )
                        )
                    }) {
                        Text("Print Kitchen")
                    }
                }


            }
        }
    }
}