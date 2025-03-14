package com.mg.barpos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mg.barpos.data.MenuList.MenuItemDao
import com.mg.barpos.data.Orders.Item
import com.mg.barpos.data.Orders.Order
import com.mg.barpos.data.Orders.OrderDao

@Database(
    entities = [Order::class, Item::class, StoredMenuItem::class, StoredExtraItem::class],
    version = 16,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class OrderDatabase: RoomDatabase() {
    abstract val orderDao: OrderDao
    abstract val menuDao: MenuItemDao

}