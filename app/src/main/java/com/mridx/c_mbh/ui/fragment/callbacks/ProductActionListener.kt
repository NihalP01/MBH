package com.mridx.c_mbh.ui.fragment.callbacks

import com.mridx.c_mbh.data.ProductData

interface ProductActionListener {

    fun addToCart(data: ProductData)

    fun buyNow(data: ProductData)

}