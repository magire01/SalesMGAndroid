package com.mg.barpos.presentation.Settings.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory
import com.mg.barpos.data.MenuList.MenuItemDao
import com.mg.barpos.data.MenuList.MenuService
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.ItemTotal
import com.mg.barpos.data.Orders.Order
import com.mg.barpos.data.Orders.OrderDao
import com.mg.barpos.data.Orders.OrderService
import com.mg.barpos.data.StoredExtraItem
import com.mg.barpos.data.StoredMenuItem
import com.mg.barpos.presentation.Settings.State.EditMenuState
import com.mg.barpos.presentation.Settings.State.StoredMenuItemEvent
import com.mg.barpos.presentation.Settings.State.TotalsEvent
import com.mg.barpos.presentation.Settings.State.TotalsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TotalsScreenViewModel(
    private val orderService: OrderService,
    private val printTotals: (list: List<ItemTotal>) -> Unit
) : ViewModel() {

    private var orderList = orderService.getOrders.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private var itemList =
        orderService.getItems.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TotalsState())
    val state =
        combine(_state, orderList, itemList) { state, orders, items ->
            state.copy(
                orderList = orders,
                itemList = items,
                itemTotals = createItemTotalsList()
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TotalsState())

    fun onTotalsEvent(event: TotalsEvent) {
        when (event) {

            is TotalsEvent.PrintTotals -> {
                viewModelScope.launch() {
                    printTotals(createItemTotalsList())
                }
            }

            is TotalsEvent.DeleteOrders -> {
                viewModelScope.launch() {
                    orderService.deleteAllOrders()
                }

                _state.update {
                    it.copy(
                        itemTotals = createItemTotalsList()
                    )
                }
            }
            else -> {}
        }
    }

    private fun createItemTotalsList(): MutableList<ItemTotal> {
        var totalsList: MutableList<ItemTotal> = mutableListOf()

        var numberOfOrders = orderList.value.size
        var totalMoney = 0.0

        for (order in orderList.value) {
            totalMoney += order.orderTotal
        }
        var moneyTotals = ItemTotal(numberOfOrders, "Orders", null, totalMoney)
        totalsList.add(moneyTotals)

        var categories = itemList.value.distinctBy { it.itemName }
        var categoryList: MutableList<Item> = mutableListOf()

        for (category in categories) {
            categoryList += category
        }

        for (category in categoryList) {
            var newList = itemList.value.filter { it.itemName == category.itemName }
            var totalPrice: Double = 0.0

            for (item in newList) {
                totalPrice += item.itemPrice
            }

            var newCategory = ItemTotal(
                numberOfItems = newList.size,
                itemName = newList.first().itemName,
                limit = null,
                total = totalPrice,
            )
            totalsList.add(newCategory)
        }

        for (item in itemList.value) {

        }

        return totalsList
    }
}