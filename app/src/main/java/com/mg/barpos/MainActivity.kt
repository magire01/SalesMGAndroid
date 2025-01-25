package com.mg.barpos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.mg.barpos.data.Converter
import com.mg.barpos.data.MenuItem
import com.mg.barpos.data.OrderDao
import com.mg.barpos.data.OrderDatabase
import com.mg.barpos.presentation.MenuViewModel
import com.mg.barpos.presentation.screens.ItemMenu
import com.mg.barpos.presentation.OrderViewModel
import com.mg.barpos.presentation.Settings.ViewModel.EditMenuViewModel
import com.mg.barpos.presentation.components.IconButton
import com.mg.barpos.presentation.screens.ConfirmOrderScreen
import com.mg.barpos.presentation.screens.MainTabScreen
import com.mg.barpos.presentation.screens.OrdersScreen
import com.mg.barpos.presentation.screens.SavedOrderDetails
import com.mg.barpos.presentation.Settings.SettingsContainer
import com.mg.barpos.ui.theme.RoomDatabaseTheme

class MainActivity : ComponentActivity() {

    val sideOptions = arrayOf("Fries", "Mac", "Beans", "Puppies", "Slaw")

    val items = arrayOf(
        MenuItem("Fish Sand Platter", 10.00, 3, sideOptions = sideOptions, selectedSides = emptyArray()),
        MenuItem("Shrimp Platter", 11.00, 3, sideOptions = sideOptions, selectedSides = emptyArray()),
        MenuItem("Tenders Platter", 9.00, 3, sideOptions = sideOptions, selectedSides = emptyArray()),
        MenuItem("Tenders Basket", 9.00, 1, sideOptions = sideOptions, selectedSides = emptyArray()),
        MenuItem("Mozz Sticks", 5.00, 0, sideOptions = emptyArray(), selectedSides = emptyArray()),
        MenuItem("Extra Shrimp", 6.00, 0, sideOptions = emptyArray(), selectedSides = emptyArray()),
        )

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            OrderDatabase::class.java,
            "orders.db"
        )
            .addTypeConverter(Converter())
            .fallbackToDestructiveMigration()
            .build()
    }

    private val viewModel by viewModels<OrderViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return OrderViewModel(database.dao) as T
                }
            }
        }
    )

    private val menuViewModel by viewModels<MenuViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return MenuViewModel(database.dao) as T
                }
            }
        }
    )

    private val editMenuViewModel by viewModels<EditMenuViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return EditMenuViewModel(database.dao) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDatabaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state by viewModel.state.collectAsState()
                    val menuState by menuViewModel.state.collectAsState()
                    val editMenuState by editMenuViewModel.uiState.collectAsState()
                    val navController = rememberNavController()
                    val tabController = rememberNavController()


                    NavHost(
                        navController= navController,
                        startDestination = "MainTabScreen",
                        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
                        exitTransition =  { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
                        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End) },
                        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End) },
                    ) {
                        composable("MainTabScreen") {
                            MainTabScreen(
                                state = state,
                                menuState = menuState,
                                navController = navController,
                                tabController = tabController,
                                onEvent = viewModel::onEvent,
                            )
                        }
                        composable("OrdersScreen") {
                            OrdersScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent,
                            )
                        }
//                        composable("ItemMenu") {
//                            ItemMenu(
//                                state = state,
//                                navController = navController)
//                        }
                        composable("SavedOrderDetails") {
                            SavedOrderDetails(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onItemEvent)
                        }
                        composable("ConfirmOrderScreen") {
                            ConfirmOrderScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent,
                                onItemEvent = viewModel::onItemEvent
                            )
                        }

                        composable("SettingsContainer") {
                            SettingsContainer(
                                navController = navController,
                                state = editMenuState,
                                onEvent = editMenuViewModel::onEvent,
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun BackButton(navController: NavController) {
    IconButton(
        description = "Back",
        imageVector = Icons.Rounded.ArrowBack
    ) {
        navController.popBackStack()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    BarPOSTheme {
//        Greeting("Android")
//    }
//}