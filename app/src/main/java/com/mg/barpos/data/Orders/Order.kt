package com.mg.barpos.data.Orders

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
    val orderName: String = "",
    val isTab: Boolean = false,
    val orderTotal: Double,
    val orderNumber: Int,

    @PrimaryKey(autoGenerate = true)
    var orderId: Int = 0
    )
