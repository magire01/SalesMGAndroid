package com.mg.barpos.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.TakeoutDining
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, val icon: ImageVector?, var title: String) {
    object OrdersScreen : NavigationItem("OrdersScreen", Icons.Rounded.Home, "Orders")
    object ItemMenu : NavigationItem("ItemMenu", Icons.Rounded.List, "Menu")
    object EditMenu : NavigationItem("CreateMenu", Icons.Rounded.Edit, "Edit Menu")
    object EditExtras : NavigationItem("EditExtras", Icons.Rounded.TakeoutDining, "Edit Extras")
    object EditPrinter : NavigationItem("EditPrinter", Icons.Rounded.Edit, "Edit Printer")
    object Totals : NavigationItem("Totals", Icons.Rounded.Balance, "Totals")
}
