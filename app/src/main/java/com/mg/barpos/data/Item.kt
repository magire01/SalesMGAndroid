package com.mg.barpos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class Item(
    val orderId: Int,
    val itemName: String,
    val itemPrice: Int,
    val sideOptions: Array<String>,
    val selectedSides: Array<String>,


    @PrimaryKey(autoGenerate = true)
    val itemNumber: Int = 0,
) {

}


