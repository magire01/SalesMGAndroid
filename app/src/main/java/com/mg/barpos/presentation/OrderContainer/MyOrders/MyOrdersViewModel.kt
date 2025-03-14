package com.mg.barpos.presentation.OrderContainer.MyOrders

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.Order
import com.mg.barpos.data.Orders.OrderService
import com.mg.barpos.presentation.ItemEvent
import com.mg.barpos.presentation.OrderEvent
import com.mg.barpos.utils.FullScreenLoadingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyOrdersViewModel(
    private val orderService: OrderService,
    private val printCustomerReceipt: (order: Order, itemList: List<Item>) -> Unit,
    private val printKitchenReceipt: (order: Order, itemList: List<Item>) -> Result<Boolean>,
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

    fun onOrderEvent(event: OrderEvent) {
        when (event) {
            is OrderEvent.PrintBluetooth -> {
                var order = state.value.orders.first { it.orderNumber == event.orderNumber }
                val itemList = event.items

                viewModelScope.launch() {
                    printCustomerReceipt(order, itemList)
                }
                _state.update {
                    it.copy(

                    )
                }
            }

            is OrderEvent.PrintNetwork -> {
                var order = state.value.orders.first { it.orderNumber == event.orderNumber }
                val itemList = event.items

                viewModelScope.launch() {
                    printKitchenReceipt(order, itemList)
                }
                _state.update {
                    it.copy(

                    )
                }
            }

            else -> {}
        }
    }

    fun onItemEvent(event: ItemEvent) {
        when (event) {

            ItemEvent.GetItemById(id = state.value.selectedOrderNumber.value) -> {
                orderId.value = state.value.selectedOrderNumber.value
            }


            else -> {}
        }
    }
}