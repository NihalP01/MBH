package com.mridx.c_mbh.ui.activity.cart

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mridx.c_mbh.Utils.AppExecutors
import com.mridx.c_mbh.database.room.db.CartDatabase
import com.mridx.c_mbh.database.room.tables.CartItem

class CartUIViewModel : ViewModel() {

    private var _cartItems = MutableLiveData<ArrayList<CartItem>>()
    val cartItems: LiveData<ArrayList<CartItem>> = _cartItems

    fun getCart(context: Context) {
        AppExecutors.getInstance().diskIO().execute {
            _cartItems.postValue(
                CartDatabase.getInstance(context).cartItemDao().getAll() as ArrayList<CartItem>
            )
        }
    }


}