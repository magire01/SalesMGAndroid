package com.mg.barpos.presentation.Settings.State

import com.mg.barpos.data.StoredMenuItem

sealed interface StoredMenuItemEvent {
    data class SaveMenuItem(
        val itemNumber: Int?,
        val itemName: String,
        val itemPrice: Double,
        val category: String,
        val numberOfSides: Int,
        val sideOptions: Array<String>,
        val selectedSides: Array<String>,
        val inStock: Boolean,
    ) : StoredMenuItemEvent

    data class DeleteMenuItem(
        val itemNumber: Int,
        val itemName: String,
        val itemPrice: Double,
        val category: String,
        val numberOfSides: Int,
        val sideOptions: Array<String>,
        val selectedSides: Array<String>,
        val inStock: Boolean,
    ) : StoredMenuItemEvent

    data class SaveExtraItem(
        val itemName: String,
        val category: String,
        val categoryLimit: Int,
    ) : StoredMenuItemEvent

    data class DeleteExtraItem(
        val extraNumber: Int,
        val itemName: String,
        val category: String,
        val categoryLimit: Int,
    ) : StoredMenuItemEvent
}
