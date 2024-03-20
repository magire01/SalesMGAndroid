package com.mg.barpos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Order::class, Item::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class OrderDatabase: RoomDatabase() {
    abstract val dao: OrderDao
}