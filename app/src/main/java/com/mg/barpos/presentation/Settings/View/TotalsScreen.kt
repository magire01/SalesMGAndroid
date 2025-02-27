package com.mg.barpos.presentation.Settings.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mg.barpos.BackButton
import com.mg.barpos.presentation.Settings.State.TotalsState
import com.mg.barpos.presentation.components.ItemRow
import com.mg.barpos.presentation.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TotalsScreen(navController: NavController, state: TotalsState) {
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
                    Text(state.itemTotals[index].numberOfItems.toString())
                    Text(state.itemTotals[index].itemName)
                    Text(state.itemTotals[index].total.toString())
                }
            }
        }
    }
}