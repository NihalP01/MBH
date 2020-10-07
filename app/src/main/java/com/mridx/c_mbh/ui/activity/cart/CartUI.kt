package com.mridx.c_mbh.ui.activity.cart

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mridx.c_mbh.R
import com.mridx.c_mbh.Utils.Utils
import com.mridx.c_mbh.adapter.CartItemAdapter
import com.mridx.c_mbh.database.room.tables.CartItem
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.cart_content_ui.*
import kotlinx.android.synthetic.main.cart_ui.*
import java.util.ArrayList

class CartUI : AppCompatActivity() {

    lateinit var viewModel: CartUIViewModel

    lateinit var cartAdapter: CartItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_ui)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Cart"
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel = ViewModelProvider(this).get(CartUIViewModel::class.java)

        setupView()

        viewModel.getCart(this)

        viewModel.cartItems.observe(this, {
            renderData(it)
        })

    }

    private fun renderData(list: ArrayList<CartItem>) {
        cartAdapter.setList(list)
        var total: Float = 0.toFloat()
        list.forEach {
            total += it.price.toFloat()
            //checkoutBtn?.text = "Total - Rs. ${Utils.decimalFormat.format(total)}"
        }
        checkoutBtn?.text = "Total - Rs. ${Utils.decimalFormat.format(total)}"
    }

    private fun setupView() {
        cartAdapter = CartItemAdapter()
        val itemHolder = findViewById<RecyclerView>(R.id.cartItemHolder)
        itemHolder.apply {
            this.adapter = cartAdapter
            this.layoutManager = LinearLayoutManager(this@CartUI)
        }
        /*cartItemHolder?.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@CartUI)
        }*/

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}