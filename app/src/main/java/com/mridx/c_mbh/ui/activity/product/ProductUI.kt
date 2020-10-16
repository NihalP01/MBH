package com.mridx.c_mbh.ui.activity.product

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.aii.adapter.callback.AdapterIdentifier
import com.mridx.c_mbh.R
import com.mridx.c_mbh.Utils.AppExecutors
import com.mridx.c_mbh.Utils.LiveSession
import com.mridx.c_mbh.Utils.Utils
import com.mridx.c_mbh.Utils.Utils.Companion.decimalFormat
import com.mridx.c_mbh.Utils.Utils.Companion.qtyList
import com.mridx.c_mbh.Utils.Utils.Companion.transition
import com.mridx.c_mbh.adapter.CartItemAdapter
import com.mridx.c_mbh.adapter.ProductAdapter
import com.mridx.c_mbh.adapter.ProductImageAdapter
import com.mridx.c_mbh.adapter.callbacks.AdapterItemClickedListener
import com.mridx.c_mbh.data.ProductData
import com.mridx.c_mbh.database.room.db.CartDatabase
import com.mridx.c_mbh.database.room.tables.CartItem
import com.mridx.c_mbh.libs.MenuBadgeHelper
import com.mridx.c_mbh.ui.activity.cart.CartUI
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.product_content_ui.*
import kotlinx.android.synthetic.main.product_ui.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductUI : AppCompatActivity(), AdapterItemClickedListener {

    lateinit var viewModel: ProductUIViewModel
    lateinit var productData: ProductData

    private var productAdapter: ProductAdapter? = null

    //private var productImageAdapter = ProductImageAdapter()
    private var weights: ArrayList<String> = ArrayList()

    lateinit var cartDatabase: CartDatabase

    private var currentPage = 0

    private var uMenu: Menu? = null
    private var qty = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_ui)

        viewModel = ViewModelProvider(this).get(ProductUIViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
        }

        cartDatabase = CartDatabase.getInstance(this)

        productData = intent.extras?.getSerializable("DATA") as ProductData

        productImageView?.apply {
            transitionName = "${transition}${productData.id}"
        }

        setupView()

        checkIfinCart()

        viewModel.addedToCart.observe(this, {
            if (it) {
                addToCartBtn?.apply {
                    text = "Go To Cart"
                    this.setOnClickListener { goToCart() }
                }
                actionCart()
                LiveSession.getInstance().getSessionData().apply {
                    this.cartItem += 1
                }.also { s ->
                    if (uMenu != null)
                        MenuBadgeHelper.addBadge(this, uMenu, s.cartItem.toString())
                    LiveSession.getInstance().cartItems(s.cartItem)
                }
            }
        })

        viewModel.similarItems.observe(this) {
            productAdapter?.setList(it)
        }

        viewModel.loadProduct(productData.id)
    }

    private fun checkIfinCart() {
        GlobalScope.launch {
            if (cartDatabase.cartItemDao().check(productData.id.toString()) != null) {
                addToCartBtn?.apply {
                    text = "Go to cart"
                    setOnClickListener { goToCart() }
                }
            } else {
                addToCartBtn?.apply {
                    text = "Add to Cart"
                    setOnClickListener { addToCart() }
                }
            }
        }
    }

    private fun goToCart() {
        startActivity(Intent(this, CartUI::class.java))
    }


    private fun setupView() {
        productInfoText?.text = productData.name
        Glide.with(this).asBitmap().load(productData.image).into(productImageView)
        productMRPView?.text =
            SpannableString("MRP Rs. ${decimalFormat.format(productData.mrp)}").apply {
                this.setSpan(
                    StrikethroughSpan(),
                    4,
                    this.lastIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        productActualPriceView?.text = "Rs. ${decimalFormat.format(productData.price)}"

        productAdapter = ProductAdapter().also { it.adapterItemClickedListener = this }
        similarProductsHolder?.apply {
            this.adapter = productAdapter
            this.layoutManager = GridLayoutManager(
                this@ProductUI,
                context.resources.getInteger(R.integer.product_column)
            ).also {
                it.orientation = RecyclerView.VERTICAL
            }
        }

        addToCartBtn?.setOnClickListener { addToCart() }
        buyNowBtn?.setOnClickListener { buyNow() }
    }

    private fun buyNow() {
        TODO("Not yet implemented")
    }

    private fun addToCart() {
        viewModel.addToCart(productData)
        //actionCart()
    }

    private fun actionCart() {
        GlobalScope.launch {
            if (cartDatabase.cartItemDao().check(productData.id.toString()) == null) {
                if (cartDatabase.cartItemDao().insertItem(
                        CartItem(
                            productData.id.toString(),
                            productData.name,
                            productData.weight,
                            qty,
                            productData.price,
                            productData.image,
                            productData.mrp,
                            productData.type
                        )
                    ) != (-1).toLong()
                ) {
                    /*val size = cartDatabase.cartItemDao().getAll().size
                    runOnUiThread {
                        LiveSession.getInstance().getSessionData().cartItem = size
                        MenuBadgeHelper.addBadge(this, getMenu(), size.toString())
                    }*/
                    Log.d("kaku", "actionCart: product added to cart and loacl db")
                }
            } else {
                /*runOnUiThread {
                    Toast.makeText(this, "Already added to cart", Toast.LENGTH_LONG).show()

                }*/
                Log.d("kaku", "actionCart: product already in cart")
            }

            /* if (cartDatabase.cartItemDao().insertItem(
                     CartItem(
                         productData.id.toString(),
                         productData.name,
                         productData.weight,
                         qty,
                         productData.price.toString()
                     )
                 ) != (-1).toLong()
             ) {
                 //val size = cartDatabase.cartItemDao().getAll().size
                 *//*runOnUiThread {
                    LiveSession.getInstance().getSessionData().cartItem = size
                    MenuBadgeHelper.addBadge(this, getMenu(), size.toString())
                }*//*
            }*/

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.action_search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = "Search here"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(this@ProductUI, "Search for $query", Toast.LENGTH_SHORT)
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

    private fun getMenu() = this.uMenu

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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
            return
        }
        super.onBackPressed()
    }

    override fun onClicked(adapterIdentifier: AdapterIdentifier, data: Any) {
        startActivity(
            Intent(this, ProductUI::class.java).also { intent ->
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


