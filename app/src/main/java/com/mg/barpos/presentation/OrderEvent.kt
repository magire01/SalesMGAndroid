package com.mg.barpos.presentation

import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.Order

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

    data class PrintBluetooth(
        val orderNumber: Int,
        val items: List<Item>
    ): OrderEvent

    data class PrintNetwork(
        val orderNumber: Int,
        val items: List<Item>
    ): OrderEvent
}

sealed interface ItemEvent {
    data class GetItemById(
        val id: Int
    ): ItemEvent
}