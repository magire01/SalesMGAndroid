package com.mg.barpos.presentation.Settings.View

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mg.barpos.data.StoredMenuItem
import com.mg.barpos.presentation.Settings.State.EditMenuState
import com.mg.barpos.presentation.components.SubmitButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddEditItemSheet(
    onDismiss: () -> Unit,
    state: EditMenuState,
    selectedItem: StoredMenuItem,
    onSubmitClick: (StoredMenuItem) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var itemCategory = remember { mutableStateOf(selectedItem.category)}
    var itemName = remember { mutableStateOf(selectedItem.itemName) }
    var itemPrice = remember { mutableStateOf(selectedItem.itemPrice) }
    var extraOptions = remember { mutableStateListOf<String>() }

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxSize(),
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Scaffold(
            Modifier
                .padding(bottom = 60.dp)
                .fillMaxSize(),
            bottomBar = {
                SubmitButton(buttonTitle = "Save Item") {
                    var submittableItem = StoredMenuItem(
                        itemName.value,
                        itemPrice.value,
                        selectedItem.category,
                        selectedItem.numberPriority,
                        0,
                        extraOptions.toTypedArray(),
                        emptyArray<String>(),
                    )
                    onSubmitClick(submittableItem)
                }
            }
        )
        { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                stickyHeader {
                    Text(
                        text = "Category: ${itemCategory.value}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }

                item {
                    ItemTextField("Item Name", itemName.value) { itemName.value = it }
                }

                item {
                    ItemTextField("Price", itemPrice.value.toString()) { itemPrice.value = it.toDouble() }
                }

                items(state.extraCategoryList.size) {index ->
                    var (name, limit) = state.extraCategoryList[index]
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = if (extraOptions.contains(name)) Color.Yellow else Color.Blue),
                        onClick = {
                            if (extraOptions.contains(name)) {
                                extraOptions -= name
                            } else {
                                extraOptions += name
                            }
                    }) {
                        Text("${name} - Limit ${limit}")
                    }
                }
            }
        }
    }
}


@Composable
fun ItemTextField(
    title: String,
    placeholder: String,
    onChange: (String) -> Unit,
) {
    Row {
        Text(title)
        TextField(value = placeholder, onValueChange = {
            onChange(it)
        })
    }

}

