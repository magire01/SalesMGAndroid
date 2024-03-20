package com.mg.barpos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface OrderDao {
    @Upsert
    suspend fun upsertItem(item: Item)

    @Upsert
    suspend fun upsertOrder(order: Order)
    @Delete
    suspend fun deleteItem(item: Item)

    @Query("SELECT * FROM `ORDER`")
    fun getOrders(): Flow<List<Order>>

    @Query("SELECT * FROM `ITEM`")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM `ITEM` where orderId=:id")
    fun getItemsById(id: Int): Flow<List<Item>>

    @Transaction
    @Query("SELECT * FROM `Order`")
    fun getOrderWithItems(): Flow<List<OrderWithItems>>


    @Query("DELETE FROM `Order`")
    suspend fun nukeTable()

}