package com.mg.barpos.data.MenuList

import com.mg.barpos.data.StoredExtraItem
import com.mg.barpos.data.StoredMenuItem

data class ExtraCategory(
    val numberPriority: Int,
    val categoryName: String,
    val categoryLimit: Int,
    val extraList: List<StoredExtraItem>,
)
