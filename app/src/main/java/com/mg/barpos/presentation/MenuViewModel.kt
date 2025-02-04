package com.mg.barpos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory
import com.mg.barpos.data.MenuList.MenuItemDao
import com.mg.barpos.data.MenuList.MenuService
import com.mg.barpos.data.Orders.OrderDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val menuService: MenuService
) : ViewModel() {
    private var orderId = MutableStateFlow(0)

    private var storedMenuItems = menuService.itemList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var extraList =
        menuService.extraList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(MenuState())
    val state =
        combine(_state, orderId, storedMenuItems, extraList) { state, orderId, _, _ ->
            state.copy(
                menuList = createMenuList(),
                extraList = createExtraList()
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MenuState())

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