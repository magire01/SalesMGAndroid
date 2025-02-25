package com.mg.barpos.presentation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory
import com.mg.barpos.data.MenuList.MenuItemDao
import com.mg.barpos.data.MenuList.MenuService
import com.mg.barpos.data.Orders.Order
import com.mg.barpos.data.Orders.OrderService
import com.mg.barpos.utils.FullScreenLoadingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderService: OrderService,
    menuService: MenuService,
    private val printReceipt: (order: Order, itemList: List<Item>) -> Unit
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
        orderService.getOrders.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var storedMenuItems = menuService.itemList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var extraList =
        menuService.extraList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var isLoading = mutableStateOf(false)

    private val _state = MutableStateFlow(OrderState())
    val state =
        combine(_state, orders, items, orderId) { state, orders, items, orderId ->
            state.copy(
                orders = orders,
                items = items,
                selectedOrderNumber = mutableIntStateOf(orderId),
                menuList = createMenuList(),
                extraList = createExtraList()
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OrderState())

    fun onEvent(event: OrderEvent) {
        when (event) {

            is OrderEvent.SaveOrder -> {
                var order = Order(
                    orderName = event.orderName,
                    isTab = event.isTab,
                    orderTotal = event.orderTotal
                )

                val itemList = event.items

                viewModelScope.launch() {
                    FullScreenLoadingManager.showLoader()
                    orderService.createOrder(order, itemList)
                    order.orderNumber = state.value.orderNumber
                    printReceipt(order, itemList)
                    FullScreenLoadingManager.hideLoader()
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

            ItemEvent.GetItemById(id = state.value.selectedOrderNumber.value) -> {
                orderId.value = state.value.selectedOrderNumber.value
            }


            else -> {}
        }
    }

    private fun createMenuList(): MutableList<MenuCategory> {
        var menuCategoryList: MutableList<MenuCategory> = mutableListOf()
        var categories = storedMenuItems.value.distinctBy { it.category }
        var categoryList: MutableList<String> = mutableListOf()

        for (category in categories) {
            categoryList += category.category
        }

        for (category in categoryList) {
            var newList = storedMenuItems.value.filter { it.category == category }

            var newCategory = MenuCategory(
                numberPriority = categoryList.size,
                categoryName = category,
                menuList = newList,
            )
            menuCategoryList.add(newCategory)
        }

        return menuCategoryList
    }

    private fun createExtraList(): MutableList<ExtraCategory> {
        var extraCategoryList: MutableList<ExtraCategory> = mutableListOf()
        var categories = extraList.value.distinctBy { it.category }
        var categoryList: MutableList<Pair<String, Int>> = mutableListOf()

        for (category in categories) {
            categoryList += Pair(category.category, category.categoryLimit)
        }

        for (category in categoryList) {
            val (name, limit) = category
            var newList = extraList.value.filter { it.category == name }

            var newCategory = ExtraCategory(
                numberPriority = categoryList.size,
                categoryName = name,
                categoryLimit = limit,
                extraList = newList,
            )
            extraCategoryList.add(newCategory)
        }

        return extraCategoryList
    }
}