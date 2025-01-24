package com.mg.barpos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StoredMenuItem(
    val itemName: String,
    val itemPrice: Double,
    val category: String,
    val numberPriority: Int,
    val numberOfSides: Int,
    val sideOptions: Array<String>,
    val selectedSides: Array<String>,

    @PrimaryKey(autoGenerate = true)
    val itemNumber: Int = 0,
)
