package com.mg.barpos.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.Navigation

sealed class NavigationItem(var route: String, val icon: ImageVector?, var title: String) {
    object OrdersScreen : NavigationItem("OrdersScreen", Icons.Rounded.Home, "Orders")
    object ItemMenu : NavigationItem("ItemMenu", Icons.Rounded.List, "Menu")
}