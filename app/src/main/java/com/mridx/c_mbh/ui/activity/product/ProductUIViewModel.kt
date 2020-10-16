package com.mridx.c_mbh.ui.activity.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mridx.c_mbh.Utils.JSONHelper
import com.mridx.c_mbh.api.ApiHandler
import com.mridx.c_mbh.api.ApiResponseListener
import com.mridx.c_mbh.api.RequestType
import com.mridx.c_mbh.data.ProductData
import org.json.JSONArray
import org.json.JSONObject

class ProductUIViewModel : ViewModel(), ApiResponseListener {

    private var _addedToCart = MutableLiveData<Boolean>()
    val addedToCart: LiveData<Boolean> = _addedToCart

    private var _similarItems = MutableLiveData<ArrayList<ProductData>>()
    val similarItems: LiveData<ArrayList<ProductData>> = _similarItems


    fun addToCart(productData: ProductData) {
        ApiHandler.instance.apply {
            this.apiResponseListener = this@ProductUIViewModel
            this.addToCart(productData.id)
        }
    }

    fun loadProduct(id: Int) = ApiHandler.instance.apply {
        this.apiResponseListener = this@ProductUIViewModel
        this.product(id)
    }

    override fun onResponse(
        success: Boolean,
        type: RequestType,
        responseObj: JSONObject?,
        responseArr: JSONArray?
    ) {
        if (!success) {
            return
        }
        when (type) {
            RequestType.ADD_CART -> {
                _addedToCart.postValue(true)
            }
            RequestType.PRODUCT -> {
                _similarItems.postValue(
                    JSONHelper.parseProducts(
                        responseObj?.getJSONObject("data")?.getJSONArray("similar_product")
                    )
                )
            }
            else -> {
            }
        }
    }


}