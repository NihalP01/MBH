package com.mridx.c_mbh.data

import java.io.Serializable

/*data class ProductData(
    val id: Int,
    val name: String,
    val images: ArrayList<String>,
    val categoryId: Int,
    val categoryName: String,
    val weights: ArrayList<Weight>
) : Serializable*/

data class ProductData(
    var id: Int,
    var name: String,
    var image: String,
    var type: String,
    var category: String,
    var weight: String,
    var price: Float,
    var mrp: Float
) : Serializable

data class Weight(val weight: String, val price: String, val discount: String, val mrp: String) :
    Serializable