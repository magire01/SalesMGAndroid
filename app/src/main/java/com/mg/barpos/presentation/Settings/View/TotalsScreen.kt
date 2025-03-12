package com.mg.barpos.presentation.Settings.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mg.barpos.BackButton
import com.mg.barpos.presentation.NavigationItem
import com.mg.barpos.presentation.Settings.State.TotalsEvent
import com.mg.barpos.presentation.Settings.State.TotalsState
import com.mg.barpos.presentation.components.ItemRow
import com.mg.barpos.presentation.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TotalsScreen(navController: NavController, state: TotalsState, onEvent: (TotalsEvent) -> Unit) {
    var deleteAlert = remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopBar(title = "Edit Printer", navController = navController) {
                BackButton(navController = navController)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = true,
        ) {
            items(state.itemTotals.size) { index ->
                Row {
                    Text(state.itemTotals[index].numberOfItems.toString(), modifier = Modifier.padding(8.dp))
                    Text(state.itemTotals[index].itemName, modifier = Modifier.padding(8.dp))
                    Text(state.itemTotals[index].total.toString(), modifier = Modifier.padding(8.dp))
                }
            }

            item {
                Row {
                    Button(onClick = {
                        onEvent(
                            TotalsEvent.PrintTotals(
                                print = true,
                            )
                        )
                    }, modifier = Modifier.padding(8.dp)) {
                        Text("Print Report")
                    }

                    Button(onClick = {
                        deleteAlert.value = true
                    }, modifier = Modifier.padding(8.dp)) {
                        Text("Delete Orders")
                    }
                }
            }
        }
        if (deleteAlert.value) {
            AlertDialog(
                title = {
                    Text(text = "Delete All Orders?")
                },
                text = {
                    Text(text = "You are about to delete all orders, you will not be able to recover them")
                },
                onDismissRequest = {
                    deleteAlert.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(
                                TotalsEvent.DeleteOrders(
                                    delete = true,
                                )
                            )
                            deleteAlert.value = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            deleteAlert.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

    }
}