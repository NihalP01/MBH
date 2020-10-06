package com.mridx.c_mbh.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mridx.c_mbh.R
import com.mridx.c_mbh.Utils.Utils
import com.mridx.c_mbh.data.CategoryData
import kotlinx.android.synthetic.main.category_view.view.*

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var list: ArrayList<CategoryData> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun setList(newList: ArrayList<CategoryData>) {
        this.list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.imageView?.setColorFilter(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.black_100
                ), PorterDuff.Mode.SRC_OVER
            )
        }

        fun bind(data: CategoryData) {
            itemView.textView.text = data.name
            Glide.with(itemView.context).load(data.url).into(itemView.imageView)
        }

    }
}