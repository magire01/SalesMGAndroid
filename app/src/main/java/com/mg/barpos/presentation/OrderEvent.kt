package com.mg.barpos.presentation

import com.mg.barpos.data.Orders.Item

sealed interface OrderEvent {
    data class SaveOrder(
        val orderName: String,
        val isTab: Boolean,
        val orderTotal: Double,
        val items: List<Item>
    ): OrderEvent
}

sealed interface ItemEvent {
    data class GetItemById(
        val id: Int
    ): ItemEvent
}