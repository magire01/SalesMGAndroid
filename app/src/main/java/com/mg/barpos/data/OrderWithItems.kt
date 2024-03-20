package com.mg.barpos.data

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithItems (
    @Embedded val orderWithItemsData: Order,
    @Relation(
        parentColumn = "orderNumber",
        entityColumn = "orderId",
    )
    val orderItems: List<Item>
)