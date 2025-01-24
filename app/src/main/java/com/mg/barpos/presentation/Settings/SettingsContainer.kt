package com.mg.barpos.presentation.Settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mg.barpos.presentation.NavigationItem
import com.mg.barpos.presentation.Settings.State.EditMenuState
import com.mg.barpos.presentation.Settings.State.StoredMenuItemEvent
import com.mg.barpos.presentation.Settings.View.EditExtraItems
import com.mg.barpos.presentation.Settings.View.EditMenu
import com.mg.barpos.presentation.Settings.View.EditPrinter

@Composable
fun SettingsContainer(
    navController: NavController,
    state: EditMenuState,
    onEvent: (StoredMenuItemEvent) -> Unit
) {
    val tabController = rememberNavController()


    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier) {
                SettingsNavigationBar(navController = tabController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                PaddingValues(
                    0.dp,
                    0.dp,
                    0.dp,
                    innerPadding.calculateBottomPadding()
                )
            )
        ) {
            SettingsNavigation(
                tabController = tabController,
                navController = navController,
                state = state,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
fun SettingsNavigation(
    navController: NavController,
    tabController: NavHostController,
    state: EditMenuState,
    onEvent: (StoredMenuItemEvent) -> Unit,
) {
    NavHost(tabController, startDestination = "CreateMenu") {
        composable(NavigationItem.EditMenu.route) {
            EditMenu(
                navController = navController,
                state = state,
                onEvent = onEvent,
            )
        }

        composable(NavigationItem.EditExtras.route) {
            EditExtraItems(
                navController = navController,
                state = state,
                onEvent = onEvent,
            )
        }

        composable("EditPrinter") {
            EditPrinter(navController = navController)
        }
    }
}

@Composable
fun SettingsNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem.EditMenu,
        NavigationItem.EditExtras,
        NavigationItem.EditPrinter
    )
    var selectedItem by remember { mutableStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.EditMenu.route) }

    items.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon!!, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    currentRoute = item.route
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
