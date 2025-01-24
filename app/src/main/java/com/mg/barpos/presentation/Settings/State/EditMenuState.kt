package com.mg.barpos.presentation.Settings.State

import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory

data class EditMenuState (
    val menuList: List<MenuCategory> = emptyList(),
    val extraList: List<ExtraCategory> = emptyList(),
    val extraCategoryList: List<Pair<String, Int>> = emptyList()
)