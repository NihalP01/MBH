package com.mridx.c_mbh.Utils

import com.mridx.c_mbh.api.Apis
import com.mridx.c_mbh.data.BannerData
import com.mridx.c_mbh.data.CategoryData
import com.mridx.c_mbh.data.ProductData
import org.json.JSONArray

class JSONHelper {

    companion object {
        fun parseBanner(response: JSONArray?): ArrayList<BannerData> {
            val list = ArrayList<BannerData>()
            if (response == null) return list

            for (i in 0 until response.length()) {
                list.add(BannerData("${Apis.image}${response.getJSONObject(i).getString("image")}"))
            }
            return list
        }

        fun parseCategories(response: JSONArray?): ArrayList<CategoryData> {
            val list = ArrayList<CategoryData>()
            if (response == null) return list

            for (i in 0 until response.length()) {
                val category = response.getJSONObject(i)
                list.add(
                    CategoryData(
                        i,
                        category.getString("category_name"),
                        "${Apis.image}${category.getString("category_image")}"
                    )
                )
            }
            return list
        }

        fun parseProducts(response: JSONArray?): ArrayList<ProductData> {
            val list = ArrayList<ProductData>()
            if (response == null) return list

            for (i in 0 until response.length()) {
                val product = response.getJSONObject(i)
                list.add(
                    ProductData(
                        product.getInt("id"),
                        product.getString("name"),
                        "${Apis.image}${product.getString("image")}",
                        if (product.has("type") && product.getString("type") != "null") {
                            product.getString("type")
                        } else "Regular",
                        product.getString("category"),
                        product.getString("weight"),
                        product.getString("price").toFloat(),
                        product.getString("mrp").toFloat(),
                    )
                )
            }
            return list
        }
    }


}