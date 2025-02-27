package com.mg.barpos.presentation.OrderContainer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mg.barpos.presentation.ItemEvent
import com.mg.barpos.presentation.OrderEvent
import com.mg.barpos.presentation.OrderState
import com.mg.barpos.presentation.components.IconButton
import com.mg.barpos.presentation.components.ItemRow
import com.mg.barpos.presentation.components.SubmitButton
import com.mg.barpos.presentation.components.TopBar
import com.mg.barpos.presentation.components.TotalRow

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
        modifier = Modifier.imePadding().fillMaxHeight(),
    topBar = {
            TopBar(
                title = "Orders",
                navController = navController
            ) {
                IconButton(
                    description = "Back",
                    imageVector = Icons.Rounded.ArrowBack,
                ) {
                    navController.popBackStack()
                }
            }
        },
        bottomBar = {
            SubmitButton(buttonTitle = "Submit Order") {
                // Create Order data
                onEvent(
                    OrderEvent.SaveOrder(
                        orderName = state.orderName.value,
                        isTab = false,
                        orderTotal = orderTotal,
                        items = state.selectedItems
                    )
                )
                navController.popBackStack()
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