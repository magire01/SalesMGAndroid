package com.mg.barpos.presentation.Settings.View

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mg.barpos.data.StoredExtraItem
import com.mg.barpos.data.StoredMenuItem
import com.mg.barpos.presentation.components.SubmitButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddEditExtraSheet(
    onDismiss: () -> Unit,
    selectedItem: StoredExtraItem,
    onSubmitClick: (StoredExtraItem) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var itemCategory = remember { mutableStateOf(selectedItem.category) }
    var itemName = remember { mutableStateOf(selectedItem.itemName) }

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
                    var submittableItem = StoredExtraItem(
                        itemName.value,
                        itemCategory.value,
                        selectedItem.categoryLimit,
                        selectedItem.numberPriority,
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
            }
        }
    }
}