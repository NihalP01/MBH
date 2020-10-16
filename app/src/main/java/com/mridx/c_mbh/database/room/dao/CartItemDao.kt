package com.mridx.c_mbh.database.room.dao

import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.mridx.c_mbh.database.room.db.CartDatabase
import com.mridx.c_mbh.database.room.tables.CartItem

@Dao
interface CartItemDao {

    @Query("SELECT * FROM " + CartItem.CART_ITEM)
    fun getAll(): List<CartItem>

    @Insert(onConflict = IGNORE)
    fun insertItem(cartItem: CartItem): Long

    @Query("SELECT * FROM " + CartItem.CART_ITEM + " WHERE item_id = :item_id")
    fun check(item_id: String): CartItem?

}