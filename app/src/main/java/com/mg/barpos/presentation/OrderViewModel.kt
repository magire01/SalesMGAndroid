package com.mg.barpos.presentation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.Item
import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory
import com.mg.barpos.data.Order
import com.mg.barpos.data.OrderDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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

    private var storedMenuItems = dao.getStoredMenuItems().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var extraList =
        dao.getStoredExtraItems().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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
                val order = Order(
                    orderName = event.orderName,
                    isTab = event.isTab,
                    orderTotal = event.orderTotal
                )

                viewModelScope.launch() {
                    state.value.isLoading.value = true
                    dao.upsertOrder(order)
                    state.value.isLoading.value = false

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
                    numberOfSides = event.numberOfSides,
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