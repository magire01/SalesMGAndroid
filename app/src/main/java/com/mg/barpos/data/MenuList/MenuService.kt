package com.mg.barpos.data.MenuList

import androidx.lifecycle.viewModelScope
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.Order
import com.mg.barpos.data.StoredExtraItem
import com.mg.barpos.data.StoredMenuItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MenuService (private val menuDao: MenuItemDao) {
    var itemList = menuDao.getStoredMenuItems()

    var extraList = menuDao.getStoredExtraItems()

    suspend fun saveMenuItem(menuItem: StoredMenuItem) {
        menuDao.upsertStoredMenuItem(menuItem)
    }

    suspend fun saveExtraItem(extraItem: StoredExtraItem) {
        menuDao.upsertStoredExtraItem(extraItem)
    }
}