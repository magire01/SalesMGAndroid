package com.mg.barpos.presentation.screens

import android.graphics.Paint.Align
import android.service.autofill.OnClickAction
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mg.barpos.data.Item
import com.mg.barpos.presentation.ItemEvent
import com.mg.barpos.presentation.OrderEvent
import com.mg.barpos.presentation.components.ItemRow
import com.mg.barpos.presentation.components.SelectedSideCard
import com.mg.barpos.presentation.components.SubmitButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddSidesSheet(
    item: Item,
    onDismiss: () -> Unit,
    onSubmitClick: (List<String>) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    var selectedSides = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            bottomBar = {
                SubmitButton(buttonTitle = "Add To Order") {
                    onSubmitClick(selectedSides)
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
                        text = "Add Sides (${item.numberOfSides})",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }

                items(item.sideOptions.size) { index ->
                    Card(
                        modifier = Modifier
                            .width(200.dp),
                        onClick = {
                            if (selectedSides.size <= item.numberOfSides - 1) {
                                selectedSides.add(item.sideOptions[index])
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Only ${item.numberOfSides} sides allowed")
                                }
                            }

                        }
                    ) {
                        Row {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(item.sideOptions[index], textAlign = TextAlign.Center)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = "Add Item"
                                )
                            }
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                    ) {
                        Row() {
                            for (side in selectedSides) {
                                Column(
                                    modifier = Modifier
                                        .width(LocalConfiguration.current.screenWidthDp.dp / item.numberOfSides),
                                ) {
                                    SelectedSideCard(
                                        sideName = side
                                    ) {
                                        selectedSides.remove(side)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

