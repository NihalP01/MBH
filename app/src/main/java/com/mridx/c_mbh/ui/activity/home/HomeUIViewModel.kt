package com.mridx.c_mbh.ui.activity.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mridx.c_mbh.adapter.BannerAdapter
import com.mridx.c_mbh.data.BannerData
import com.mridx.c_mbh.data.CategoryData
import com.mridx.c_mbh.data.ProductData
import com.mridx.c_mbh.data.Weight

class HomeUIViewModel : ViewModel() {

    private var _bannerList: MutableLiveData<ArrayList<BannerData>> =
        MutableLiveData<ArrayList<BannerData>>()
    val bannerList: LiveData<ArrayList<BannerData>> = _bannerList

    private var _categoryList: MutableLiveData<ArrayList<CategoryData>> =
        MutableLiveData<ArrayList<CategoryData>>()
    val categoryList: LiveData<ArrayList<CategoryData>> = _categoryList

    private var _productList: MutableLiveData<ArrayList<ProductData>> = MutableLiveData()
    val productList: LiveData<ArrayList<ProductData>> = _productList


    fun getBanners() {
    }


    fun setupHome(context: Context) {
        val list = ArrayList<BannerData>()
        list.add(BannerData(url = "https://img.freepik.com/free-vector/offline-twitch-banner-concept_23-2148588101.jpg?size=626&ext=jpg"))
        list.add(BannerData(url = "https://image.freepik.com/free-vector/offline-twitch-banner-template_23-2148600905.jpg"))
        list.add(BannerData(url = "https://d2ct9xspam8wud.cloudfront.net/blog/2019/05/21152931/TwitchGamemanias.jpg"))

        _bannerList.apply {
            value = list
        }

        val cList = ArrayList<CategoryData>()
        cList.add(
            CategoryData(
                1,
                "Grocery",
                "https://img.freepik.com/free-vector/offline-twitch-banner-concept_23-2148588101.jpg?size=626&ext=jpg"
            )
        )
        cList.add(
            CategoryData(
                2,
                "Non-Veg",
                "https://image.freepik.com/free-vector/offline-twitch-banner-template_23-2148600905.jpg"
            )
        )
        _categoryList.apply { value = cList }

        val pList = ArrayList<ProductData>()
        for (i in 0 until 20) {
            val l = ArrayList<Weight>()
            l.add(Weight("${250 + i}", "${50 + i}", "${i * 2}", "${60 + i}"))
            l.add(Weight("${500 + i}", "${100 + i}", "${i * 5}", "${120 + i}"))

            val images = ArrayList<String>()
            images.add("https://www.bigbasket.com/media/uploads/p/l/10000903_10-fresho-chicken-leg-boneless-antibiotic-residue-free-growth-hormone-free-2-3-pcs.jpg")

            pList.add(
                ProductData(
                    i + 1,
                    "Chicken $i",
                    images,
                    2,
                    "Non-Veg",
                    l
                )
            )
        }
        _productList.value = pList

    }

}
