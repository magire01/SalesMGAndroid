package com.mg.barpos.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mg.barpos.data.Item
import com.mg.barpos.data.MenuItem
import com.mg.barpos.presentation.OrderState
import com.mg.barpos.presentation.components.MenuCard
import com.mg.barpos.presentation.components.SelectedItemRow
import com.mg.barpos.presentation.components.SelectedTotalRow
import com.mg.barpos.presentation.components.TopBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemMenu(
    state: OrderState,
    navController: NavController,
) {
    var selectedItemList = remember { mutableStateListOf<Item>() }
    var popupControl by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Menu",
                button = null,
                navController = navController
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.selectedItems = selectedItemList
                state.orderNumber = state.orders.size + 1
                navController.navigate("ConfirmOrderScreen")
            }) {
                Icon(imageVector = Icons.Rounded.AttachMoney, contentDescription = "Start Order")
            }
        }
    ) { paddingValues ->
        Row {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.menuList.forEach { category ->
                    stickyHeader {
                        Row {
                            Text(text = category.categoryName)
                        }
                    }
                    items(category.menuList.size) { index ->

                        MenuCard(
                            itemName = category.menuList[index].itemName,
                            itemPrice = category.menuList[index].itemPrice.toString()
                        ) {
                            selectedItem = Item(
                                orderId = state.orders.size + 1,
                                itemName = category.menuList[index].itemName,
                                itemPrice = category.menuList[index].itemPrice,
                                numberOfSides = category.menuList[index].numberOfSides,
                                sideOptions = category.menuList[index].sideOptions,
                                selectedSides = category.menuList[index].selectedSides,
                            )
                            if (category.menuList[index].numberOfSides > 0) {
                                popupControl = true
                            } else {
                                selectedItem?.let {
                                    selectedItemList.add(
                                        Item(
                                            orderId = state.orders.size + 1,
                                            itemName = it.itemName,
                                            itemPrice = it.itemPrice,
                                            numberOfSides = it.numberOfSides,
                                            sideOptions = it.sideOptions,
                                            selectedSides = it.selectedSides,
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }


            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(selectedItemList.size) { index ->
                    SelectedItemRow(
                        itemName = selectedItemList[index].itemName,
                        itemPrice = selectedItemList[index].itemPrice.toString(),
                        selectedSides = selectedItemList[index].selectedSides
                    ) {
                        selectedItemList.remove(selectedItemList[index])
                    }
                }
                item {
                    if (!selectedItemList.isEmpty()) {
                        var total = 0.00
                        for (x in selectedItemList) {
                            total += x.itemPrice
                        }
                        SelectedTotalRow(orderTotal = total)
                    }
                }
            }

            if (popupControl) {
                selectedItem?.let {
                    AddSidesSheet(
                        item = it,
                        onDismiss = {
                            popupControl = false
                        },
                        onSubmitClick = { list ->
                            selectedItemList.add(
                                Item(
                                    orderId = state.orders.size + 1,
                                    itemName = it.itemName,
                                    itemPrice = it.itemPrice,
                                    numberOfSides = it.numberOfSides,
                                    sideOptions = it.sideOptions,
                                    selectedSides = list.toTypedArray(),
                                )
                            )
                            popupControl = false
                        }
                    )
                }
            }
        }
    }
}


