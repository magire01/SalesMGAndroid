package com.mg.barpos

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.TypeConverters
import com.mg.barpos.data.Converter
import com.mg.barpos.data.MenuItem
import com.mg.barpos.data.OrderDatabase
import com.mg.barpos.presentation.screens.ItemMenu
import com.mg.barpos.presentation.OrderViewModel
import com.mg.barpos.presentation.screens.ConfirmOrderScreen
import com.mg.barpos.presentation.screens.MainTabScreen
import com.mg.barpos.presentation.screens.OrdersScreen
import com.mg.barpos.presentation.screens.SavedOrderDetails
import com.mg.barpos.ui.theme.RoomDatabaseTheme

class MainActivity : ComponentActivity() {

    val sideOptions = arrayOf("Fries", "Mac", "Beans", "Puppies", "Slaw")

    val items = arrayOf(
        MenuItem("Fish Sand Platter", 10, true, sideOptions = sideOptions, selectedSides = emptyArray()),
        MenuItem("Shrimp Platter", 11, true, sideOptions = sideOptions, selectedSides = emptyArray()),
        MenuItem("Tenders Platter", 9, true, sideOptions = sideOptions, selectedSides = emptyArray()),
        MenuItem("Mozz Sticks", 5, false, sideOptions = emptyArray(), selectedSides = emptyArray()),
        MenuItem("Extra Shrimp", 6, false, sideOptions = emptyArray(), selectedSides = emptyArray()),
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
                    val navController = rememberNavController()
                    val tabController = rememberNavController()


                    NavHost(navController= navController, startDestination = "MainTabScreen") {
                        composable("MainTabScreen") {
                            MainTabScreen(
                                state = state,
                                navController = navController,
                                tabController = tabController,
                                onEvent = viewModel::onEvent,
                                items = items,
                            )
                        }
                        composable("OrdersScreen") {
                            OrdersScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent,
                            )
                        }
                        composable("ItemMenu") {
                            ItemMenu(
                                state = state,
                                navController = navController,
                                items = items)
                        }
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

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    BarPOSTheme {
//        Greeting("Android")
//    }
//}