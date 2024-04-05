package com.mg.barpos.presentation

sealed interface OrderEvent {
    data class SaveOrder(
        val orderName: String,
        val isTab: Boolean,
        val orderTotal: Double,
    ): OrderEvent
}

sealed interface ItemEvent {
    data class SaveItem(
        val orderId: Int,
        val itemName: String,
        val itemPrice: Double,
        val numberOfSides: Int,
        val sideOptions: Array<String>,
        val selectedSides: Array<String>
    ): ItemEvent

    data class GetItemById(
        val id: Int
    ): ItemEvent
}