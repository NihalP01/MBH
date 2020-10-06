package com.mridx.c_mbh.ui.activity.home

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.imageview.ShapeableImageView
import com.mridx.c_mbh.R
import com.mridx.c_mbh.Utils.AppExecutors
import com.mridx.c_mbh.Utils.LiveSession
import com.mridx.c_mbh.Utils.Utils.Companion.sliderDelay
import com.mridx.c_mbh.adapter.BannerAdapter
import com.mridx.c_mbh.adapter.CategoryAdapter
import com.mridx.c_mbh.adapter.ProductAdapter
import com.mridx.c_mbh.adapter.callbacks.AdapterItemClickedListener
import com.mridx.c_mbh.data.ProductData
import com.mridx.c_mbh.database.room.db.CartDatabase
import com.mridx.c_mbh.ui.activity.product.ProductUI
import kotlinx.android.synthetic.main.content_ui.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeUI : AppCompatActivity() {

    lateinit var viewModel: HomeUIViewModel

    private var bannerAdapter: BannerAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var productAdapter: ProductAdapter? = null

    private var currentPage = 0
    private var bannerItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_ui)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar.apply {
            title = getString(R.string.app_name)
        }


        viewModel = ViewModelProvider(this).get(HomeUIViewModel::class.java)

        setupView()

        viewModel.setupHome(this)

        viewModel.bannerList.observe(this, {
            bannerAdapter?.setList(it)
            bannerItems = it.size
            setupIndicator().also {
                setupCurrentIndicator(0)
            }
            startSlide(sliderDelay)
        })
        viewModel.categoryList.observe(this, {
            categoryAdapter?.setList(it)
        })
        viewModel.productList.observe(this, {
            productAdapter?.setList(it)
        })

        AppExecutors.getInstance().diskIO().execute {
            LiveSession.getInstance().getSessionData().cartItem =
                CartDatabase.getInstance(this).cartItemDao().getAll().size
        }


    }

    private fun setupView() {
        bannerAdapter = BannerAdapter()
        sliderView?.apply {
            adapter = bannerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setupCurrentIndicator(position)
                }
            })
        }

        categoryAdapter = CategoryAdapter()
        categoryView?.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(this@HomeUI).also {
                it.orientation = LinearLayoutManager.HORIZONTAL
            }
        }

        productAdapter = ProductAdapter().also {
            it.adapterItemClickedListener = object : AdapterItemClickedListener {
                override fun onClicked(data: Any) {
                    val intent =
                        Intent(this@HomeUI, ProductUI::class.java).also { intent ->
                            intent.putExtra("DATA", data as ProductData)
                        }
                    startActivity(intent)
                }

                override fun onLongClicked(data: Any) {
                }
            }
        }
        itemHolder?.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(
                this@HomeUI,
                context.resources.getInteger(R.integer.product_column)
            ).also {
                it.orientation = GridLayoutManager.VERTICAL
            }
        }

    }


    //region Banner

    private fun startSlide(i: Long) {
        GlobalScope.launch {
            delay(1000 * i)
            sliderView?.apply {
                if (currentPage == bannerItems) currentPage = 0
                setCurrentItem(currentPage++, true)
            }
            startSlide(sliderDelay)
        }
    }

    private fun setupIndicator() {
        val indicator: Array<ShapeableImageView?> = arrayOfNulls(bannerItems)
        val layoutParams = LinearLayoutCompat.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(4, 0, 4, 0)

        for (i in indicator.indices) {
            indicator[i] = ShapeableImageView(applicationContext).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.indicator_inactive)
                )
                this.layoutParams = layoutParams
            }
            //indicator[i].setLayoutParams(layoutParams)
            indicatorHolder?.addView(indicator[i])
        }
    }

    private fun setupCurrentIndicator(index: Int) {
        val count: Int = indicatorHolder?.childCount ?: 0
        for (i in 0 until count) {
            if (i == index) {
                (indicatorHolder?.getChildAt(i) as ShapeableImageView).setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.indicator_active)
                )
            } else {
                (indicatorHolder?.getChildAt(i) as ShapeableImageView).setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.indicator_inactive)
                )
            }
        }
    }

    //endregion


}