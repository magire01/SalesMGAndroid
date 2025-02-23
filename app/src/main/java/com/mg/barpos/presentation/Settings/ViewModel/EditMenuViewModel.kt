package com.mg.barpos.presentation.Settings.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory
import com.mg.barpos.data.MenuList.MenuItemDao
import com.mg.barpos.data.MenuList.MenuService
import com.mg.barpos.data.Orders.OrderDao
import com.mg.barpos.data.StoredExtraItem
import com.mg.barpos.data.StoredMenuItem
import com.mg.barpos.presentation.Settings.State.EditMenuState
import com.mg.barpos.presentation.Settings.State.StoredMenuItemEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditMenuViewModel(
    private val menuService: MenuService
) : ViewModel() {

    private var itemList =
        menuService.itemList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var extraList =
        menuService.extraList.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _uiState = MutableStateFlow(EditMenuState())
    val uiState =
        combine(_uiState, itemList, extraList) { uiState, _, _ ->
            uiState.copy(
                menuList = createMenuList(),
                extraList = createExtraList(),
                extraCategoryList = createCategoryList()
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EditMenuState())

    fun onEvent(event: StoredMenuItemEvent) {
        when (event) {

            is StoredMenuItemEvent.SaveMenuItem -> {
                val menuItem = StoredMenuItem(
                    itemName = event.itemName,
                    itemPrice = event.itemPrice,
                    category = event.category,
                    numberPriority = itemList.value.filter { it.category == event.category }.size,
                    numberOfSides = event.numberOfSides,
                    sideOptions = event.sideOptions,
                    selectedSides = event.selectedSides,
                )

                viewModelScope.launch() {
                    var findItem = itemList.value.find {it.itemNumber == event.itemNumber }
                    if (findItem == null) {
                        menuService.saveMenuItem(menuItem)
                    } else {
                        menuService.updateMenuItem(menuItem)
                    }
                }
                _uiState.update {
                    it.copy(
                        menuList = createMenuList()
                    )
                }
            }

            is StoredMenuItemEvent.SaveExtraItem -> {
                val extraItem = StoredExtraItem(
                    itemName = event.itemName,
                    category = event.category,
                    numberPriority = extraList.value.filter { it.category == event.category }.size,
                    categoryLimit = event.categoryLimit,
                )

                viewModelScope.launch() {
                    menuService.saveExtraItem(extraItem)
                }
                _uiState.update {
                    it.copy(
                        extraList = createExtraList()
                    )
                }
            }
            else -> {}
        }
    }

    private fun createMenuList(): MutableList<MenuCategory> {
        var menuCategoryList: MutableList<MenuCategory> = mutableListOf()
        var categories = itemList.value.distinctBy { it.category }
        var categoryList: MutableList<String> = mutableListOf()

        for (category in categories) {
            categoryList += category.category
        }

        for (category in categoryList) {
            var newList = itemList.value.filter { it.category == category }

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

    private fun createCategoryList(): MutableList<Pair<String, Int>> {
        var categories = extraList.value.distinctBy { it.category }
        var categoryList: MutableList<Pair<String, Int>> = mutableListOf()

        for (category in categories) {
            categoryList += Pair(category.category, category.categoryLimit)
        }

        return categoryList
    }
}