package com.mridx.c_mbh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mridx.c_mbh.R
import com.mridx.c_mbh.data.BannerData
import kotlinx.android.synthetic.main.banner_view.view.*

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {


    private var list = ArrayList<BannerData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.banner_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BannerAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun setList(newList: ArrayList<BannerData>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: BannerData) {
            Glide.with(itemView.context).load(data.url).into(itemView.imageView)
        }
    }

}