package com.example.mediatorlivedata.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {

    @Query("SELECT * FROM ItemEntity")
    fun getItems(): LiveData<List<ItemEntity>>

    @Insert
    suspend fun insertItem(item: ItemEntity)

    @Insert
    fun insertItem(items: List<ItemEntity>)
}