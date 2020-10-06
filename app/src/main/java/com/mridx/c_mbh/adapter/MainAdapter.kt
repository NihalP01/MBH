package com.mridx.c_mbh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mridx.c_mbh.R
import com.mridx.c_mbh.data.ImageData
import kotlinx.android.synthetic.main.image_view.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var list: ArrayList<ImageData> = ArrayList()

    var onItemClicked: ((Int, ImageData, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @JvmName("setList1")
    fun addItems(list: ArrayList<ImageData>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClicked?.invoke(adapterPosition, list[adapterPosition], it.imageView)
            }
        }

        fun bind(imageData: ImageData) {
            Glide.with(itemView.context).load(imageData.url).into(itemView.imageView)
            itemView.textView.text = imageData.name
            ViewCompat.setTransitionName(itemView.imageView, imageData.name)
        }


    }


}