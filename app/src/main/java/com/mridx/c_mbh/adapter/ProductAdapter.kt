package com.mridx.c_mbh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.aii.adapter.callback.AdapterIdentifier
import com.mridx.c_mbh.R
import com.mridx.c_mbh.Utils.Utils.Companion.decimalFormat
import com.mridx.c_mbh.Utils.Utils.Companion.transition
import com.mridx.c_mbh.adapter.callbacks.AdapterItemClickedListener
import com.mridx.c_mbh.data.ProductData
import com.mridx.c_mbh.data.Weight
import kotlinx.android.synthetic.main.product_image_view.view.*
import kotlinx.android.synthetic.main.product_view.view.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var adapterItemClickedListener: AdapterItemClickedListener? = null

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
            itemView.productUnitView?.text = data.weight
            itemView.productTypeView?.text = data.type
            itemView.labelText?.text = data.type
            itemView.productPriceView?.text = "Rs. ${decimalFormat.format(data.price)}"
            Glide.with(itemView.context).asBitmap().load(data.image)
                .into(itemView.productImageView)
            itemView.productImageView.transitionName = "${transition}${data.id}"
            itemView.setOnClickListener {
                adapterItemClickedListener?.onClickedWithTransition(
                    AdapterIdentifier.PRODUCT_ADAPTER,
                    data,
                    itemView.productImageView
                )
            }
        }

    }

}