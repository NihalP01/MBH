package com.mridx.c_mbh.ui.activity.product

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.imageview.ShapeableImageView
import com.mridx.c_mbh.R
import com.mridx.c_mbh.Utils.AppExecutors
import com.mridx.c_mbh.Utils.LiveSession
import com.mridx.c_mbh.adapter.ProductImageAdapter
import com.mridx.c_mbh.data.ProductData
import com.mridx.c_mbh.data.Weight
import com.mridx.c_mbh.database.room.db.CartDatabase
import com.mridx.c_mbh.database.room.tables.CartItem
import com.mridx.c_mbh.libs.MenuBadgeHelper
import com.mridx.c_mbh.ui.activity.cart.CartUI
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_ui.*
import kotlinx.android.synthetic.main.product_content_ui.*
import kotlinx.android.synthetic.main.product_content_ui.indicatorHolder
import kotlinx.android.synthetic.main.product_ui.*

class ProductUI : AppCompatActivity() {

    lateinit var videoModel: ProductUIViewModel
    lateinit var productData: ProductData
    private var productImageAdapter = ProductImageAdapter()
    private var weights: ArrayList<String> = ArrayList()

    lateinit var cartDatabase: CartDatabase

    private var currentPage = 0

    lateinit var uMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_ui)

        videoModel = ViewModelProvider(this).get(ProductUIViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
        }

        cartDatabase = CartDatabase.getInstance(this)

        productData = intent.extras?.getSerializable("DATA") as ProductData

        setupView()

    }

    private fun setupView() {
        productInfoText?.text = productData.name

        productImageHolder?.apply {
            this.adapter = productImageAdapter
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setupCurrentIndicator(position)
                }
            })
        }

        productImageAdapter.setList(productData.images).also {
            setupIndicator().also {
                setupCurrentIndicator(0)
            }
        }

        productData.weights.forEach { weight ->
            weights.add("${weight.weight} Gram")
        }

        productUnitSelector?.apply {
            this.adapter = ArrayAdapter(
                this@ProductUI,
                R.layout.quantity_view,
                weights
            )
            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    setupPriceView(productData.weights[p2])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

        }

        addToCartBtn?.setOnClickListener { addToCart() }
        buyNowBtn?.setOnClickListener { buyNow() }
    }

    private fun buyNow() {
        TODO("Not yet implemented")
    }

    private fun addToCart() {
        AppExecutors.getInstance().diskIO().execute {
            if (cartDatabase.cartItemDao().insertItem(
                    CartItem(
                        productData.id.toString(),
                        productData.name,
                        productData.weights[0].weight,
                        2,
                        productData.weights[0].price
                    )
                ) != (-1).toLong()
            ) {
                val size = cartDatabase.cartItemDao().getAll().size
                runOnUiThread {
                    LiveSession.getInstance().getSessionData().cartItem = size
                    MenuBadgeHelper.addBadge(this, getMenu(), size.toString())
                }
            }

        }
    }

    private fun setupPriceView(weight: Weight) {
        productActualPriceView?.text = "Rs. ${weight.price}"
        productMRPView?.apply {
            text = "MRP Rs. ${weight.mrp}"
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        productDiscountView?.text = "Flat Rs. ${(weight.mrp).toInt() - (weight.price).toInt()} OFF"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.cart -> startActivity(Intent(this, CartUI::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    fun getMenu() = this.uMenu

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            this.uMenu = menu
        }
        MenuBadgeHelper.addBadge(
            this,
            menu,
            LiveSession.getInstance().getSessionData().cartItem.toString()
        )
        return super.onPrepareOptionsMenu(menu)
    }


    //region slider indicator
    private fun setupIndicator() {
        val indicator: Array<ShapeableImageView?> = arrayOfNulls(productData.weights.size)
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


