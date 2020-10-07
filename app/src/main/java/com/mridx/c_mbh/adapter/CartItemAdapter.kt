package com.mridx.c_mbh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mridx.c_mbh.R
import com.mridx.c_mbh.database.room.tables.CartItem
import kotlinx.android.synthetic.main.cart_product_view.view.*

class CartItemAdapter : RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    private var list = ArrayList<CartItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_product_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartItemAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun setList(newList: ArrayList<CartItem>) {
        this.list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: CartItem) {
            itemView.itemName.text = data.item_name
            itemView.itemUnit.text = data.unit
            itemView.productActualPriceView.text = data.price
            itemView.productMRPView.text = data.price
            itemView.productDiscountView.text = "Rs. 20 OFF"
        }

    }
}