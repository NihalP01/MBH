package com.mridx.c_mbh.ui.activity.home_ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mridx.c_mbh.Utils.JSONHelper
import com.mridx.c_mbh.api.ApiHandler
import com.mridx.c_mbh.api.ApiResponseListener
import com.mridx.c_mbh.api.RequestType
import com.mridx.c_mbh.data.BannerData
import com.mridx.c_mbh.data.CategoryData
import com.mridx.c_mbh.data.ProductData
import com.mridx.c_mbh.data.Weight
import org.json.JSONArray
import org.json.JSONObject

class HomeUIViewModel : ViewModel(), ApiResponseListener {

    private var _bannerList: MutableLiveData<ArrayList<BannerData>> =
        MutableLiveData<ArrayList<BannerData>>()
    val bannerList: LiveData<ArrayList<BannerData>> = _bannerList

    private var _categoryList: MutableLiveData<ArrayList<CategoryData>> =
        MutableLiveData<ArrayList<CategoryData>>()
    val categoryList: LiveData<ArrayList<CategoryData>> = _categoryList

    private var _productList: MutableLiveData<ArrayList<ProductData>> = MutableLiveData()
    val productList: LiveData<ArrayList<ProductData>> = _productList

    private var isHome = false


    fun setupHome() {
        ApiHandler.instance.apply {
            apiResponseListener = this@HomeUIViewModel
        }.also {
            it.home()
        }
    }

    override fun onResponse(
        success: Boolean,
        type: RequestType,
        responseObj: JSONObject?,
        responseArr: JSONArray?
    ) {
        if (!success) {
            Log.d("kaku", "onResponse: $responseObj")
            return
        }
        when (type) {
            RequestType.HOME -> {
                isHome = true
                _bannerList.postValue(
                    JSONHelper.parseBanner(
                        responseObj?.getJSONObject("data")?.getJSONArray("Banner")
                    )
                )
                _categoryList.postValue(
                    JSONHelper.parseCategories(
                        responseObj?.getJSONObject("data")?.getJSONArray("Category")
                    )
                )
                _productList.postValue(
                    JSONHelper.parseProducts(
                        responseObj?.getJSONObject("data")?.getJSONArray("Product")
                    )
                )
            }
            else -> {
            }
        }
    }

}
