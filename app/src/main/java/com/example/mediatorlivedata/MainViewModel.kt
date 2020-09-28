package com.example.mediatorlivedata

import android.content.Context
import androidx.lifecycle.*
import com.example.mediatorlivedata.db.AppDatabase
import com.example.mediatorlivedata.db.ItemEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class MainViewModel(context: Context) : ViewModel() {

    private var db = AppDatabase.getInstance(context)
    private var firstInit: Boolean = true
    val newPostTracker = MediatorLiveData<Boolean>()
    val displayedItem = MutableLiveData<List<ItemEntity>>()
    val isLoading = MutableLiveData<Boolean>()

    private val items: LiveData<List<ItemEntity>> = runBlocking { db.getItemDao().getItems() }

    fun refreshItem() {
        isLoading.value = true
        newPostTracker.value = false
        displayedItem.value = items.value
        isLoading.value = false
    }

    init {
        newPostTracker.value = false
        newPostTracker.addSource(items) {
            if(firstInit) {
                firstInit = false
                displayedItem.value = it
            }
            else {
                if (newPostTracker.value == false && isDifferent()) {
                    newPostTracker.value = true
                }
            }
        }
    }

    private fun isDifferent() : Boolean = displayedItem.value?.count() ?: 0 !== items.value?.count() ?: 0

    fun insertRandomItem() {
        viewModelScope.launch {
            var item = ItemEntity(UUID.randomUUID().toString(), random(), Random().nextInt(500))
            db.getItemDao().insertItem(item)
        }
    }

    fun random(): String {
        val generator = Random()
        val randomStringBuilder = StringBuilder()
        val randomLength = generator.nextInt(10)
        var tempChar: Char
        for (i in 0 until randomLength) {
            tempChar = (generator.nextInt(96) + 32).toChar()
            randomStringBuilder.append(tempChar)
        }
        return randomStringBuilder.toString()
    }
}