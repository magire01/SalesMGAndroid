package com.mg.barpos.presentation

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.Item
import com.mg.barpos.data.Order
import com.mg.barpos.data.OrderDao
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(
    private val dao: OrderDao
) : ViewModel() {


    private var orderId = MutableStateFlow(0)

    private var items = orderId.flatMapLatest { id ->
        if (id > 0) {
            dao.getItemsById(id)
        } else {
            dao.getItems()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private var orders =
        dao.getOrders().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(OrderState())
    val state =
        combine(_state, orders, items, orderId) { state, orders, items, orderId ->
            state.copy(
                orders = orders,
                items = items,
                selectedOrderNumber = mutableIntStateOf(orderId)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OrderState())

    fun onEvent(event: OrderEvent) {
        when (event) {

            is OrderEvent.SaveOrder -> {
                val order = Order(
                    orderName = event.orderName,
                    isTab = event.isTab,
                )

                viewModelScope.launch() {
                    dao.upsertOrder(order)
                }
                _state.update {
                    it.copy(
                        orderNumber = state.value.orders.size,
                        orderName = mutableStateOf(""),
                    )
                }
            }
            else -> {}
        }
    }

    fun onItemEvent(event: ItemEvent) {
        when (event) {

            is ItemEvent.SaveItem -> {

                val item = Item(
                    orderId = event.orderId,
                    itemName = event.itemName,
                    itemPrice = event.itemPrice,
                    sideOptions = event.sideOptions,
                    selectedSides = event.selectedSides
                )

                viewModelScope.launch {
                    dao.upsertItem(item)
                }
                _state.update {
                    it.copy(
                        orderId = mutableIntStateOf(0),
                        itemName = mutableStateOf(""),
                        itemPrice = mutableIntStateOf(0),
                        itemNumber = mutableIntStateOf(0),

                    )
                }
            }

            ItemEvent.GetItemById(id = state.value.selectedOrderNumber.value) -> {
                orderId.value = state.value.selectedOrderNumber.value
            }


            else -> {}
        }
    }
}