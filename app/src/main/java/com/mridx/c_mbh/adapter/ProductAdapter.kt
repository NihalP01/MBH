package com.mridx.c_mbh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mridx.c_mbh.R
import com.mridx.c_mbh.data.ProductData
import com.mridx.c_mbh.data.Weight
import kotlinx.android.synthetic.main.product_view.view.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var list: ArrayList<ProductData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun setList(newList: ArrayList<ProductData>) {
        this.list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: ProductData) {
            itemView.productNameView?.text = data.name
            itemView.productUnitView?.text = data.weights[0].weight
            itemView.productPriceView?.text = data.weights[0].price
            Glide.with(itemView.context).load(data.images[0]).into(itemView.productImageView)
        }

    }

}