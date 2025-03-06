package com.mg.barpos.presentation

import com.mg.barpos.data.Orders.Item

sealed interface OrderEvent {
    data class SaveOrder(
        val orderName: String,
        val isTab: Boolean,
        val orderTotal: Double,
        val items: List<Item>
    ): OrderEvent

    data class PrintOrder(
        val success: Boolean,
    )
}

sealed interface ItemEvent {
    data class GetItemById(
        val id: Int
    ): ItemEvent
}