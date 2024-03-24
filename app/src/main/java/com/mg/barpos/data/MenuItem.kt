package com.mg.barpos.data

data class MenuItem(
    val itemName: String,
    val itemPrice: Double,
    val hasSides: Boolean,
    val sideOptions: Array<String>,
    val selectedSides: Array<String>,
)
