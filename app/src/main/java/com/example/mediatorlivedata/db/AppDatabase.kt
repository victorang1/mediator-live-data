package com.example.mediatorlivedata.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mediatorlivedata.common.ioThread
import java.util.*

@Database(entities = arrayOf(ItemEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getItemDao(): ItemDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "AppDatabase.db")
            .addCallback(seedDatabaseCallback(context))
            .build()

        private fun seedDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        var itemDao = getInstance(context)!!.getItemDao()
                        var items = mutableListOf<ItemEntity>()
                        for(i in 1..100) {
                            items.add(ItemEntity(
                                UUID.randomUUID().toString(),
                                "Item " + i,
                                Random().nextInt(100) + 1))
                        }
                        itemDao.insertItem(items)
                    }
                }
            }
        }
    }
}