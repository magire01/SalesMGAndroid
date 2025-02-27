package com.mg.barpos

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_PRIVILEGED
import android.Manifest.permission.BLUETOOTH_SCAN
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.mg.barpos.data.Converter
import com.mg.barpos.data.MenuItem
import com.mg.barpos.data.MenuList.MenuService
import com.mg.barpos.data.OrderDatabase
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.Order
import com.mg.barpos.data.Orders.OrderService
import com.mg.barpos.presentation.MenuViewModel
import com.mg.barpos.presentation.OrderContainer.ConfirmOrderScreen
import com.mg.barpos.presentation.OrderContainer.MainTabScreen
import com.mg.barpos.presentation.OrderContainer.MyOrders.MyOrdersViewModel
import com.mg.barpos.presentation.OrderContainer.MyOrders.SavedOrderDetails
import com.mg.barpos.presentation.OrderViewModel
import com.mg.barpos.presentation.Settings.SettingsContainer
import com.mg.barpos.presentation.Settings.ViewModel.EditMenuViewModel
import com.mg.barpos.presentation.Settings.ViewModel.TotalsScreenViewModel
import com.mg.barpos.presentation.components.FullScreenLoader
import com.mg.barpos.presentation.components.IconButton
import com.mg.barpos.ui.theme.RoomDatabaseTheme
import java.io.OutputStream
import java.io.PrintWriter
import java.net.Socket


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

    private val orderService by lazy { OrderService(database.orderDao) }
    private val menuService by lazy { MenuService(database.menuDao) }

    private val myOrdersViewModel by viewModels<MyOrdersViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return MyOrdersViewModel(orderService) as T
                }
            }
        }
    )
    private val viewModel by viewModels<OrderViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return OrderViewModel(orderService, menuService, ::printReceipt) as T
                }
            }
        }
    )

    private val menuViewModel by viewModels<MenuViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return MenuViewModel(menuService) as T
                }
            }
        }
    )

    private val editMenuViewModel by viewModels<EditMenuViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return EditMenuViewModel(menuService) as T
                }
            }
        }
    )

    private val totalsScreenViewModel by viewModels<TotalsScreenViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return TotalsScreenViewModel(orderService, ::printReceipt) as T
                }
            }
        }
    )

    fun printToIP(ipAddress: String, port: Int, message: String) {
        Thread {
            val printer = EscPosPrinter(TcpConnection(ipAddress, port), 203, 48f, 32)
            printer.printFormattedTextAndCut(
                "[C]<u><font size='big'>ORDER N°058</font></u>\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99€\n" +
                        "[L]  + Size : S\n" +
                        "[L]\n" +
                        "[L]<b>AWESOME SHOES</b>[R]24.99€\n" +
                        "[L]  + Size : 42\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[R]TOTAL PRICE :[R]34.98€\n" +
                        "[L]\n" +
                        "[C]Thank you !\n"
            )
        }.start()
    }

    private fun printNetwork() {
        val ipAddress = "192.168.1.87" // Replace with the target IP address
        val port = 9100 // Replace with the target port
//        val message = createReceipt()

        printToIP(ipAddress, port, "TEST")
        println("Message sent to $ipAddress:$port")
    }

    private fun printBluetoothTest() {
        val bluetoothConnection: BluetoothConnection? = BluetoothPrintersConnections.selectFirstPaired()
        if (bluetoothConnection != null) {
            val printer = EscPosPrinter(bluetoothConnection, 203, 48f, 32)

            // Printing commands
            printer.printFormattedText("TEST")

            printer.disconnectPrinter()

        }
    }

    private fun printReceipt(order: Order, itemList: List<Item>) {
        val bluetoothConnection: BluetoothConnection? = BluetoothPrintersConnections.selectFirstPaired()
        if (bluetoothConnection != null) {
            val printer = EscPosPrinter(bluetoothConnection, 203, 48f, 32)

            // Printing commands
            printer.printFormattedText(createReceipt(order, itemList))

            printer.disconnectPrinter()

        }
    }

    private fun createReceipt(order: Order, itemList: List<Item>): String {
        var items = ""
        for (item in itemList) {
            items += "${item.itemName} - " + "[R]$${item.itemPrice}0\n"
            for (side in item.selectedSides) {
                items += "+${side}  "
            }
            items += "\n"
        }
        var header: String =  "[C]<font size='small'>Covington Fire Department</font>" +
                "\n" +
                "[C]<font size='normal'>Fish Fry</font>" +
                "\n" +
                "[C]<font size='big'>Order #${order.orderNumber}</font>\n" +
                "[C]<font size='normal'>Name: ${order.orderName}</font>\n" +
                "\n" +
                "${items}" +
                "\n" +
                "------------ \n" +
                "[L]<b>Total: - " + "[R]$${order.orderTotal}0</b>" +
                "\n \n" +
                "[C]Thank you, see you next Friday!" +
                "\n \n" +
                "-------------" +
                "\n\n\n\n"

        return(header)
    }

    private fun createKitchenReceipt(order: Order, itemList: List<Item>): String {
        var items = ""
        var alphabet = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")
        for (index in itemList.indices) {
            items += "${alphabet[index]}) ${itemList[index].itemName} - " + "[R]$${itemList[index].itemPrice}0\n"
            for (side in itemList[index].selectedSides) {
                items += "+${side}  "
            }
            items += "\n"
        }
        var header: String =  "[C]<font size='small'>Covington Fire Department</font>" +
                "\n" +
                "[C]<font size='normal'>Fish Fry</font>" +
                "\n" +
                "[C]<font size='big'>Order #${order.orderNumber}</font>\n" +
                "[C]<font size='normal'>Name: ${order.orderName}</font>\n" +
                "\n" +
                "${items}" +
                "\n" +
                "------------ \n" +
                "[L]<b>Total: - " + "[R]$${order.orderTotal}0</b>" +
                "\n \n" +
                "[C]Thank you, see you next Friday!" +
                "\n \n" +
                "-------------" +
                "\n\n\n\n"

        return(header)
    }



    private fun doPrint() {
        printBluetoothTest()
//        printNetwork()
    }

    private val PERMISSIONS_STORAGE = arrayOf<String>(
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE,
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION,
        ACCESS_LOCATION_EXTRA_COMMANDS,
        BLUETOOTH_SCAN,
        BLUETOOTH_CONNECT,
        BLUETOOTH_PRIVILEGED
    )
    private val PERMISSIONS_LOCATION = arrayOf<String>(
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION,
        ACCESS_LOCATION_EXTRA_COMMANDS,
        BLUETOOTH_SCAN,
        BLUETOOTH_CONNECT,
        BLUETOOTH_PRIVILEGED
    )

    private fun checkPermissions() {
        val permission1 =
            ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ActivityCompat.checkSelfPermission(this, BLUETOOTH_SCAN)
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                1
            )
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_LOCATION,
                1
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
        setContent {
            RoomDatabaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state by viewModel.state.collectAsState()
                    val myOrdersState by myOrdersViewModel.state.collectAsState()
                    val menuState by menuViewModel.state.collectAsState()
                    val editMenuState by editMenuViewModel.uiState.collectAsState()
                    val navController = rememberNavController()
                    val tabController = rememberNavController()

                    FullScreenLoader()
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
                                myOrdersState = myOrdersState,
                                navController = navController,
                                tabController = tabController,
                                onEvent = viewModel::onEvent,
                                onMenuEvent = menuViewModel::onEvent
                            )
                        }

                        composable("SavedOrderDetails") {
                            SavedOrderDetails(
                                state = myOrdersState,
                                navController = navController,
                                onEvent = myOrdersViewModel::onItemEvent)
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
                                print = { doPrint() }
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