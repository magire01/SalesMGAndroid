package com.mg.barpos.presentation

import androidx.room.PrimaryKey
import com.mg.barpos.data.Orders.Item

sealed interface MenuEvent {
    data class SelectedMenuItem(
        val itemName: String,
        val itemPrice: Double,
        val category: String,
        val numberPriority: Int,
        val numberOfSides: Int,
        val sideOptions: Array<String>,
        val selectedSides: Array<String>,
        val itemNumber: Int = 0,
    ): MenuEvent
}
