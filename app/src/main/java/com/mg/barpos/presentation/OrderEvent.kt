package com.mg.barpos.presentation

import com.mg.barpos.data.Item
import com.mg.barpos.data.Order
import kotlinx.coroutines.flow.Flow

sealed interface OrderEvent {
    data class SaveOrder(
        val orderName: String,
        val isTab: Boolean,
    ): OrderEvent
}

sealed interface ItemEvent {
    data class SaveItem(
        val orderId: Int,
        val itemName: String,
        val itemPrice: Int,
        val sideOptions: Array<String>,
        val selectedSides: Array<String>
    ): ItemEvent

    data class GetItemById(
        val id: Int
    ): ItemEvent
}