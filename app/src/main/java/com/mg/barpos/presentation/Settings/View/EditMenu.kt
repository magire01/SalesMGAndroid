package com.mg.barpos.presentation.Settings.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mg.barpos.BackButton
import com.mg.barpos.data.StoredMenuItem
import com.mg.barpos.presentation.Settings.State.EditMenuState
import com.mg.barpos.presentation.Settings.State.StoredMenuItemEvent
import com.mg.barpos.presentation.components.TopBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditMenu(
    navController: NavController,
    state: EditMenuState,
    onEvent: (StoredMenuItemEvent) -> Unit,
) {
    var popupControl by remember { mutableStateOf(false) }
    var newCategory = remember { mutableStateOf(false)}
    var newCategoryName = remember { mutableStateOf("")}
    var newItem = remember { mutableStateOf(false)}
    var selectedItem = remember {
        mutableStateOf(
            StoredMenuItem(itemName = "",
                itemPrice = 0.00,
                category = newCategoryName.value,
                numberPriority = 0,
                numberOfSides = 0,
                sideOptions = emptyArray(),
                selectedSides = emptyArray(),
                itemNumber = 0))
    }

    Scaffold (
        topBar = {
            TopBar(title = "Edit Menu", navController = navController) {
                BackButton(navController = navController)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = true,
            ) {

                state.menuList.forEach { category ->
                    stickyHeader {
                        Row {
                            Text(text = category.categoryName)
                            Button(onClick = {
                                selectedItem.value = StoredMenuItem(itemName = "",
                                    itemPrice = 0.00,
                                    category = newCategoryName.value,
                                    numberPriority = 0,
                                    numberOfSides = 0,
                                    sideOptions = emptyArray(),
                                    selectedSides = emptyArray(),
                                    itemNumber = 0)
                                newCategoryName.value = ""
                                newItem.value = false
                                popupControl = true
                            }) {
                                Text(text = "Add Item")
                            }
                        }

                    }
                    items(category.menuList.size) { index ->
                        OutlinedCard(
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            onClick = {
                                selectedItem.value = category.menuList[index]
                                popupControl = true
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()


                            ) {
                                Text(
                                    category.menuList[index].itemNumber.toString(),
                                    modifier = Modifier.padding(20.dp)
                                )

                                Text(
                                    category.menuList[index].itemName,
                                    modifier = Modifier.padding(20.dp)
                                )

                                Text(
                                    category.menuList[index].itemPrice.toString(),
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (newCategory.value) {
                Row {
                    TextField(value = newCategoryName.value, onValueChange = {
                        if (newCategoryName.value.isEmpty()) {
                            newItem.value = true
                        }
                        newCategoryName.value = it
                    })

                    if (newItem.value) {
                        Button(onClick = {
                            selectedItem.value = StoredMenuItem("", 0.00, newCategoryName.value, 0, 0, emptyArray(), emptyArray())
                            newCategoryName.value = ""
                            newItem.value = false
                            popupControl = true
                        }) {
                            Text(text = "Add Item")
                        }
                    }
                }


            }

            Button(onClick = { newCategory.value = true }) {
                Text(text = "Add Section")
            }

            if (popupControl) {
                AddEditItemSheet(
                    onDismiss = { popupControl = false },
                    state = state,
                    selectedItem = selectedItem.value,
                    onSubmitClick = {
                        onEvent(
                            StoredMenuItemEvent.SaveMenuItem(
                                itemName = it.itemName,
                                itemPrice = it.itemPrice,
                                category = it.category,
                                numberOfSides = it.numberOfSides,
                                sideOptions = it.sideOptions,
                                selectedSides = it.selectedSides,
                                itemNumber = it.itemNumber,
                                inStock = it.inStock
                            )
                        )
                        popupControl = false
                    },
                    onDeleteClick = {
                        onEvent(
                            StoredMenuItemEvent.DeleteMenuItem(
                                itemName = it.itemName,
                                itemPrice = it.itemPrice,
                                category = it.category,
                                numberOfSides = it.numberOfSides,
                                sideOptions = it.sideOptions,
                                selectedSides = it.selectedSides,
                                itemNumber = it.itemNumber,
                                inStock = it.inStock
                            )
                        )
                        popupControl = false
                    }
                )
            }
        }
    }
}