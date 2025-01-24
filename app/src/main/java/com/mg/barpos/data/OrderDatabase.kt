package com.mg.barpos.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Order::class, Item::class, StoredMenuItem::class, StoredExtraItem::class],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class OrderDatabase: RoomDatabase() {
    abstract val dao: OrderDao
}