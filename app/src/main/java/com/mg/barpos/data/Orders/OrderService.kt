package com.mg.barpos.data.Orders

import kotlinx.coroutines.flow.Flow

class OrderService(private val dao: OrderDao) {
    val getOrders: Flow<List<Order>> = dao.getOrders()
    val getItems: Flow<List<Item>> = dao.getItems()

    fun getItemsById(orderId: Int): Flow<List<Item>> {
        return dao.getItemsById(orderId)
    }

    suspend fun createOrder(order: Order, itemList: List<Item>) {
        saveOrder(order)
        for (item in itemList) {
            saveItem(item)
        }
    }

    private suspend fun saveOrder(order: Order) {
        dao.upsertOrder(order)
    }

    private suspend fun saveItem(item: Item) {
        dao.upsertItem(item)
    }

    suspend fun deleteAllOrders() {
        dao.deleteOrders()
        dao.deleteItems()
    }
}