package com.mridx.c_mbh.ui.activity.home_ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dev.aii.adapter.callback.AdapterIdentifier
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
import com.mridx.c_mbh.libs.MenuBadgeHelper
import com.mridx.c_mbh.ui.activity.cart.CartUI
import com.mridx.c_mbh.ui.activity.product.ProductUI
import kotlinx.android.synthetic.main.content_ui.*
import kotlinx.android.synthetic.main.content_ui.indicatorHolder
import kotlinx.android.synthetic.main.product_content_ui.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeUI : AppCompatActivity(), AdapterItemClickedListener {

    lateinit var viewModel: HomeUIViewModel

    private var bannerAdapter: BannerAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var productAdapter: ProductAdapter? = null

    private var currentPage = 0
    private var bannerItems = 0
    private var umenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_ui)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar.apply {
            title = getString(R.string.app_name)
        }

        viewModel = ViewModelProvider(this).get(HomeUIViewModel::class.java)

        Log.d("kaku", "onCreate: ${LiveSession.getInstance().getUserId()}")

        setupView()

        viewModel.setupHome()

        viewModel.bannerList.observe(this, {
            bannerAdapter?.setList(it)
            bannerItems = it.size
            if (indicatorHolder?.childCount != bannerItems) {
                setupIndicator().also {
                    setupCurrentIndicator(0)
                }
                startSlide(sliderDelay)
            }
        })
        viewModel.categoryList.observe(this, {
            categoryAdapter?.setList(it)
        })
        viewModel.productList.observe(this, {
            productAdapter?.setList(it)
        })

        LiveSession.cartItems.observe(this) {
            if (umenu != null) {
                MenuBadgeHelper.addBadge(this, umenu, it.toString())
            }
        }

        GlobalScope.launch {
            /*LiveSession.getInstance().getSessionData().cartItem =
                CartDatabase.getInstance(this@HomeUI).cartItemDao().getAll().size*/
            CartDatabase.getInstance(this@HomeUI).cartItemDao().getAll().size.also {
                LiveSession.getInstance().getSessionData().cartItem = it
                LiveSession.getInstance().cartItems(it)
            }
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
            it.adapterItemClickedListener = this
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        this.umenu = menu
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.action_search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = "Search here"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(this@HomeUI, "Search for $query", Toast.LENGTH_SHORT)
                        .show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.cart -> startActivity(Intent(this, CartUI::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


    //region Banner

    @Synchronized
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
        if (indicatorHolder?.childCount == bannerItems) return
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

    override fun onBackPressed() {
        super.onBackPressed()
        //oneStepBack()
    }

    private fun oneStepBack() {
        val fts: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount >= 1) {
            fragmentManager.popBackStackImmediate()
            fts.commit()
        } else {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show()
        }
    }

    override fun onClicked(adapterIdentifier: AdapterIdentifier, data: Any) {
        startActivity(
            Intent(this@HomeUI, ProductUI::class.java).also { intent ->
                intent.putExtra("DATA", data as ProductData)
            })
    }

    override fun onClickedWithTransition(
        adapterIdentifier: AdapterIdentifier,
        data: Any,
        sharedView: View
    ) {
        when (adapterIdentifier) {
            AdapterIdentifier.PRODUCT_ADAPTER -> {
                startActivity(
                    Intent(this, ProductUI::class.java).also {
                        it.putExtras(Bundle().apply {
                            this.putSerializable("DATA", data as ProductData)
                        })
                    },
                    ViewCompat.getTransitionName(sharedView)?.let {
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this,
                            sharedView,
                            it
                        ).toBundle()
                    }
                )
            }
            else -> {
            }
        }
    }

    override fun onLongClicked(adapterIdentifier: AdapterIdentifier, data: Any) {
    }

}