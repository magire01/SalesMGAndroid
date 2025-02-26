package com.mg.barpos.presentation.Settings.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mg.barpos.BackButton
import com.mg.barpos.presentation.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditPrinter(navController: NavController, print: () -> Unit) {
    Scaffold (
        topBar = {
            TopBar(title = "Edit Printer", navController = navController) {
                BackButton(navController = navController)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Button(onClick = {
                print()
            }) {
                Text(text = "AddPrinter1")
            }
            Button(onClick = {

            }) {
                Text(text = "AddPrinter2")
            }
        }
    }
}