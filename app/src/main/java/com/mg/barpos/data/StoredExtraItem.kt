package com.mg.barpos.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mg.barpos.data.MenuList.MenuCategory

@Entity
data class StoredExtraItem(
    val itemName: String,
    val category: String,
    val categoryLimit: Int,
    val numberPriority: Int,

    @PrimaryKey(autoGenerate = true)
    val extraNumber: Int = 0,
)

