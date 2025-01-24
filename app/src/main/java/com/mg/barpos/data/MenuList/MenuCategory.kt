package com.mg.barpos.data.MenuList

import com.mg.barpos.data.StoredMenuItem
data class MenuCategory(
    val numberPriority: Int,
    val categoryName: String,
    val menuList: List<StoredMenuItem>,
)
