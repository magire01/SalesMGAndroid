package com.mg.barpos.data.MenuList

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.mg.barpos.data.StoredExtraItem
import com.mg.barpos.data.StoredMenuItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Upsert
    suspend fun upsertStoredMenuItem(item: StoredMenuItem)

    @Update
    suspend fun updateStoredMenuItem(item: StoredMenuItem)

    @Query("SELECT * FROM `StoredMenuItem`")
    fun getStoredMenuItems(): Flow<List<StoredMenuItem>>

    @Upsert
    suspend fun upsertStoredExtraItem(item: StoredExtraItem)

    @Query("SELECT * FROM `StoredExtraItem`")
    fun getStoredExtraItems(): Flow<List<StoredExtraItem>>
}