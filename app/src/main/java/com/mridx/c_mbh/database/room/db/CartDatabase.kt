package com.mridx.c_mbh.database.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mridx.c_mbh.database.room.dao.CartItemDao
import com.mridx.c_mbh.database.room.tables.CartItem

@Database(entities = [CartItem::class], exportSchema = false, version = 1)
abstract class CartDatabase : RoomDatabase() {

    companion object {
        private var _instance: CartDatabase? = null
        private val DB_NAME = "cart_items_db*"

        @Synchronized
        fun getInstance(context: Context): CartDatabase {
            if (_instance == null) {
                _instance = Room.databaseBuilder(
                    context.applicationContext,
                    CartDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return _instance as CartDatabase
        }

    }

    abstract fun cartItemDao(): CartItemDao


}
