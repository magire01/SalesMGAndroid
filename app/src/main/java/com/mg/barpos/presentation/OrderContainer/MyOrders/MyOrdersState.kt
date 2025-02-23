package com.mg.barpos.presentation.OrderContainer.MyOrders

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.Order

data class MyOrdersState(
    val orders: List<Order> = emptyList(),
    val items: List<Item> = emptyList(),
    var selectedOrderNumber: MutableState<Int> = mutableIntStateOf(0),
    val orderName: MutableState<String> = mutableStateOf(""),
    var orderTotal: MutableState<Double> = mutableDoubleStateOf(0.00),
)
