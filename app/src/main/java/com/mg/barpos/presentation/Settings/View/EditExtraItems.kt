package com.mg.barpos.presentation.Settings.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowLeft
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.mg.barpos.data.StoredExtraItem
import com.mg.barpos.data.StoredMenuItem
import com.mg.barpos.presentation.Settings.State.EditMenuState
import com.mg.barpos.presentation.Settings.State.StoredMenuItemEvent
import com.mg.barpos.presentation.components.TopBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditExtraItems(
    navController: NavController,
    state: EditMenuState,
    onEvent: (StoredMenuItemEvent) -> Unit
) {
    var popupControl by remember { mutableStateOf(false) }
    var newCategory = remember { mutableStateOf(false) }
    var newCategoryName = remember { mutableStateOf("") }
    var newCategoryLimit = remember { mutableStateOf(1)}
    var newItem = remember { mutableStateOf(false) }
    var selectedItem = remember { mutableStateOf(StoredExtraItem("", "", 0, 0,0)) }
    Scaffold (
        topBar = {
            TopBar(title = "Edit Extras", navController = navController) {
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

                state.extraList.forEach { category ->
                    stickyHeader {
                        Row {
                            Text(text = category.categoryName)
                            Button(onClick = {
                                selectedItem.value = StoredExtraItem("", category.categoryName, category.categoryLimit, 0,0)
                                newCategoryName.value = ""
                                newItem.value = false
                                popupControl = true
                            }) {
                                Text(text = "Add Item")
                            }
                        }

                    }
                    items(category.extraList.size) { index ->
                        OutlinedCard(
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            onClick = {
                                selectedItem.value = category.extraList[index]
                                popupControl = true
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()


                            ) {
                                Text(
                                    category.extraList[index].extraNumber.toString(),
                                    modifier = Modifier.padding(20.dp)
                                )

                                Text(
                                    category.extraList[index].itemName,
                                    modifier = Modifier.padding(20.dp)
                                )

                                Text(
                                    category.extraList[index].categoryLimit.toString(),
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    }
                }

                item {

                    Row {
                        if (newCategory.value) {
                            Row {
                                TextField(value = newCategoryName.value, onValueChange = {
                                    if (newCategoryName.value.isEmpty()) {
                                        newItem.value = true
                                    }
                                    newCategoryName.value = it
                                })

                                IconButton(onClick = {
                                    newCategoryLimit.value = newCategoryLimit.value - 1
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowLeft,
                                        contentDescription = "minus",
                                        modifier = Modifier.size(35.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Text(newCategoryLimit.value.toString())

                                IconButton(onClick = {
                                    newCategoryLimit.value = newCategoryLimit.value + 1
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowRight,
                                        contentDescription = "minus",
                                        modifier = Modifier.size(35.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                if (newItem.value) {
                                    Button(onClick = {
                                        selectedItem.value = StoredExtraItem("", newCategoryName.value, 0, 0,0)
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
                    }
                }
            }

            if (popupControl) {
                AddEditExtraSheet(
                    onDismiss = { popupControl = false },
                    selectedItem = selectedItem.value,
                    onSubmitClick = {
                        onEvent(
                            StoredMenuItemEvent.SaveExtraItem(
                                itemName = it.itemName,
                                category = it.category,
                                categoryLimit = newCategoryLimit.value
                            )
                        )
                        popupControl = false
                    },
                    onDeleteClick = {
                        onEvent(
                            StoredMenuItemEvent.DeleteExtraItem(
                                extraNumber = it.extraNumber,
                                itemName = it.itemName,
                                category = it.category,
                                categoryLimit = newCategoryLimit.value
                            )
                        )
                        popupControl = false
                    }
                )
            }
        }
    }
}