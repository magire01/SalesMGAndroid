package com.mg.barpos.presentation.Settings.View

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.mg.barpos.BackButton
import com.mg.barpos.presentation.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditPrinter(navController: NavController) {
    Scaffold (
        topBar = {
            TopBar(title = "Edit Printer", navController = navController) {
                BackButton(navController = navController)
            }
        }
    ) {
        Text("Create Menu")
    }
}