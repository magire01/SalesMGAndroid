package com.mg.barpos.data

import androidx.room.Embedded
import androidx.room.Relation
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.Order

data class OrderWithItems (
    @Embedded val orderWithItemsData: Order,
    @Relation(
        parentColumn = "orderNumber",
        entityColumn = "orderId",
    )
    val orderItems: List<Item>
)