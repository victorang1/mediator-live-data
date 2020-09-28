package com.example.mediatorlivedata.db

import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity (
    @PrimaryKey
    val itemID: String,
    val itemName: String,
    val itemStock: Int
) : BaseObservable()