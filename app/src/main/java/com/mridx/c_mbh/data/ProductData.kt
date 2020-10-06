package com.mridx.c_mbh.data

data class ProductData(
    val id: Int,
    val name: String,
    val images: ArrayList<String>,
    val categoryId: Int,
    val categoryName: String,
    val weights: ArrayList<Weight>
)

data class Weight(val weight: String, val price: String, val discount: String, val mrp: String)