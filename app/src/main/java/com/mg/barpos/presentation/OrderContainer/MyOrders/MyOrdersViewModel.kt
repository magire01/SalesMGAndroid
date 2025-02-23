package com.mg.barpos.presentation.OrderContainer.MyOrders

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.Orders.OrderService
import com.mg.barpos.presentation.ItemEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class MyOrdersViewModel(
    private val orderService: OrderService
) : ViewModel() {
    private var orderId = MutableStateFlow(0)
    private var items = orderId.flatMapLatest { id ->
        if (id > 0) {
            orderService.getItemsById(id)
        } else {
            orderService.getItems
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var orders =
        orderService.getOrders.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _state = MutableStateFlow(MyOrdersState())
    val state =
        combine(_state, orders, items, orderId) { state, orders, items, orderId ->
            state.copy(
                orders = orders,
                items = items,
                selectedOrderNumber = mutableIntStateOf(orderId),
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MyOrdersState())

    fun onItemEvent(event: ItemEvent) {
        when (event) {

            ItemEvent.GetItemById(id = state.value.selectedOrderNumber.value) -> {
                orderId.value = state.value.selectedOrderNumber.value
            }


            else -> {}
        }
    }
}