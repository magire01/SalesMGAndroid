package com.mg.barpos.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mg.barpos.data.MenuList.ExtraCategory
import com.mg.barpos.data.MenuList.MenuCategory

data class MenuState(
    var selectedOrderName: MutableState<String> = mutableStateOf(""),
    val menuList: List<MenuCategory> = emptyList(),
    val extraList: List<ExtraCategory> = emptyList(),
)
