package com.mg.barpos.data.Orders

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
    val orderName: String = "",
    val isTab: Boolean = false,
    val orderTotal: Double,

    @PrimaryKey(autoGenerate = true)
    val orderNumber: Int = 0
    )
