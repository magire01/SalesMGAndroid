package com.mg.barpos.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.mg.barpos.data.MenuItem
import com.mg.barpos.presentation.ItemEvent
import com.mg.barpos.presentation.NavigationItem
import com.mg.barpos.presentation.OrderEvent
import com.mg.barpos.presentation.OrderState

/*
state = state,
navController = navController,
onEvent = viewModel::onEvent,
items = items,
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabScreen(
    state: OrderState,
    navController: NavController,
    tabController: NavHostController,
    onEvent: (OrderEvent) -> Unit,
    onItemEvent: (ItemEvent) -> Unit,
    items: Array<MenuItem>
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier) {
                BottomNavigationBar(navController = tabController)
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
            Navigations(
                state = state,
                navController = navController,
                tabController = tabController,
                onEvent = onEvent,
                onItemEvent = onItemEvent,
                items = items
                )
        }
    }
}

@Composable
fun Navigations(
    state: OrderState,
    navController: NavController,
    tabController: NavHostController,
    onEvent: (OrderEvent) -> Unit,
    onItemEvent: (ItemEvent) -> Unit,
    items: Array<MenuItem>
    ) {
    NavHost(tabController, startDestination = NavigationItem.OrdersScreen.route) {
        composable(NavigationItem.OrdersScreen.route) {
            OrdersScreen(
                state = state,
                navController = navController,
                onEvent = onEvent,
                onItemEvent = onItemEvent
            )
        }
        composable(NavigationItem.ItemMenu.route) {
            ItemMenu(
                state = state,
                navController = navController,
                items = items
            )
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem.OrdersScreen,
        NavigationItem.ItemMenu,
    )
    var selectedItem by remember { mutableStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.OrdersScreen.route) }

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
