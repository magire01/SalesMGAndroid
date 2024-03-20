package com.mg.barpos.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.mg.barpos.data.Item
import com.mg.barpos.data.Order

data class OrderState(
    val orders: List<Order> = emptyList(),
    val items: List<Item> = emptyList(),
    var orderNumber: Int = 0,
    val orderName: MutableState<String> = mutableStateOf(""),
    val isTab: MutableState<Boolean> = mutableStateOf(false),
    val orderId: MutableState<Int> = mutableIntStateOf(0),
    val itemName: MutableState<String> = mutableStateOf(""),
    val itemPrice: MutableState<Int> = mutableIntStateOf(0),
    val itemNumber: MutableState<Int> = mutableIntStateOf(0),
    var selectedOrderNumber: MutableState<Int> = mutableIntStateOf(0),
    var selectedItems: List<Item> = emptyList(),
)