package com.mg.barpos.presentation

import android.content.res.TypedArray
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory
import com.mg.barpos.data.MenuList.MenuItemDao
import com.mg.barpos.data.MenuList.MenuService
import com.mg.barpos.data.Orders.Order
import com.mg.barpos.data.Orders.OrderDao
import com.mg.barpos.data.StoredMenuItem
import com.mg.barpos.utils.FullScreenLoadingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val menuService: MenuService
) : ViewModel() {

    private var storedMenuItems = menuService.itemList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var extraList =
        menuService.extraList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var selectedMenuItem = MutableStateFlow(StoredMenuItem("", 0.0, "", 0, 0, emptyArray(), emptyArray()))

    private val _state = MutableStateFlow(MenuState())
    val state =
        combine(_state, storedMenuItems, extraList, selectedMenuItem) { state, _, _, selectedMenuItem ->
            state.copy(
                menuList = createMenuList(),
                extraList = createExtraList(selectedMenuItem.sideOptions)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MenuState())

    fun onEvent(event: MenuEvent) {
        when (event) {

            is MenuEvent.SelectedMenuItem -> {
                val selectedItem = StoredMenuItem(
                    itemName = event.itemName,
                    category = event.category,
                    itemPrice = event.itemPrice,
                    numberOfSides = event.numberOfSides,
                    sideOptions = event.sideOptions,
                    selectedSides = event.selectedSides,
                    numberPriority = event.numberPriority,
                )

                selectedMenuItem.value = selectedItem
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

    private fun createExtraList(sideOptions: Array<String>): MutableList<ExtraCategory> {
        var optionList: MutableList<String> = sideOptions.toMutableList()
        var extraCategoryList: MutableList<ExtraCategory> = mutableListOf()
        var categories = extraList.value.distinctBy { it.category }
        var categoryList: MutableList<Pair<String, Int>> = mutableListOf()

        for (category in categories) {
            if (optionList.contains(category.category)) {
                categoryList += Pair(category.category, category.categoryLimit)
            }
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