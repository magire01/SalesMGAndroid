package com.mg.barpos.presentation.Settings.State

import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.ItemTotal
import com.mg.barpos.data.Orders.Order

data class TotalsState (
    val orderList: List<Order> = emptyList(),
    val itemList: List<Item> = emptyList(),
    val extraList: List<String> = emptyList(),
    val itemTotals: List<ItemTotal> = emptyList()
)