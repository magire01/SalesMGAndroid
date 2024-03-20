package com.mg.barpos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
    val orderName: String = "",
    val isTab: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val orderNumber: Int = 0
    )
