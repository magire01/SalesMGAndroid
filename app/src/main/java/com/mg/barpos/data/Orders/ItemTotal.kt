package com.mg.barpos.data.Orders

import com.mg.barpos.data.MenuList.ExtraCategory

data class ItemTotal(
    val numberOfItems: Int,
    val itemName: String,
    val limit: Int?,
    val total: Double?
)
