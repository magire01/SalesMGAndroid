package com.mg.barpos.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mg.barpos.R
import com.mg.barpos.presentation.ItemEvent
import com.mg.barpos.presentation.OrderEvent
import com.mg.barpos.presentation.OrderState
import com.mg.barpos.presentation.components.ItemRow
import com.mg.barpos.presentation.components.TotalRow
import kotlin.reflect.KFunction1

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConfirmOrderScreen(
    state: OrderState,
    navController: NavController,
    onEvent: (OrderEvent) -> Unit,
    onItemEvent: (ItemEvent) -> Unit,
) {
    var orderTotal: Double = 0.00
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Sort Notes",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                IconButton(onClick = {
                    //Navigate to Settings
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        bottomBar = {
            OutlinedButton (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 100.dp,
                        vertical = 20.dp,
                    ),
                onClick = {
                    // Create Order data
                    onEvent(
                        OrderEvent.SaveOrder(
                            orderName = state.orderName.value,
                            isTab = false,
                            orderTotal = orderTotal
                        )
                    )

                    // Create Item data
                    for (item in state.selectedItems) {
                        onItemEvent(
                            ItemEvent.SaveItem(
                                orderId = state.orderNumber,
                                itemName = item.itemName,
                                itemPrice = item.itemPrice,
                                sideOptions = item.sideOptions,
                                selectedSides = item.selectedSides
                            )
                        )
                    }
                    navController.popBackStack()
                }
            ) {
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = "Submit Order",
                    style = MaterialTheme.typography.titleMedium,
//                        color = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Order #" + state.orderNumber.toString(),
                    style = MaterialTheme.typography.titleMedium,
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp),
                    value = state.orderName.value,
                    singleLine = true,
                    onValueChange = {
                        state.orderName.value = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                    label = { Text("Customer Name") }
                )
            }
            items(state.selectedItems.size) { index ->
                ItemRow(
                    itemName = state.selectedItems[index].itemName,
                    itemPrice = state.selectedItems[index].itemPrice.toString(),
                    selectedSides = state.selectedItems[index].selectedSides
                )
                orderTotal += state.selectedItems[index].itemPrice

            }
            item {
                TotalRow(orderTotal = orderTotal)
            }
        }
    }
}