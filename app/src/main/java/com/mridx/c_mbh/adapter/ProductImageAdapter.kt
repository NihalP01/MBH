package com.mridx.c_mbh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mridx.c_mbh.R
import kotlinx.android.synthetic.main.product_image_view.view.*

class ProductImageAdapter : RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {

    private var list = ArrayList<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductImageAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_image_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductImageAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun setList(newList: ArrayList<String>) {
        this.list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(url: String) {
            Glide.with(itemView.context).load(url).into(itemView.imageView)
        }
    }
}