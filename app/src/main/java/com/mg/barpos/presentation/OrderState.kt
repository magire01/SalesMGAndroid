package com.mg.barpos.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory
import com.mg.barpos.data.Orders.Order

data class OrderState(
    val orders: List<Order> = emptyList(),
    val items: List<Item> = emptyList(),
    var orderNumber: Int = 0,
    val orderName: MutableState<String> = mutableStateOf(""),
    val isTab: MutableState<Boolean> = mutableStateOf(false),
    var orderTotal: MutableState<Double> = mutableDoubleStateOf(0.00),
    val orderId: MutableState<Int> = mutableIntStateOf(0),
    val itemName: MutableState<String> = mutableStateOf(""),
    val itemPrice: MutableState<Int> = mutableIntStateOf(0),
    val itemNumber: MutableState<Int> = mutableIntStateOf(0),
    var selectedOrderNumber: MutableState<Int> = mutableIntStateOf(0),
    var selectedOrderName: MutableState<String> = mutableStateOf(""),
    var selectedItems: List<Item> = emptyList(),
    val menuList: List<MenuCategory> = emptyList(),
    val extraList: List<ExtraCategory> = emptyList(),
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
)
