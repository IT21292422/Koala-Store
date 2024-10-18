package com.project.ecommerceapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.project.ecommerceapp.Adapter.ProductAdapter
import com.project.ecommerceapp.Model.ItemModel
import com.project.ecommerceapp.R
import com.project.ecommerceapp.databinding.ActivityAllProductsBinding
import com.project.ecommerceapp.network.API
import com.project.ecommerceapp.network.RetrofitInstance
import com.project.ecommerceapp.order.OrderHistoryActivity
import com.project.ecommerceapp.profile.CustomerProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProducts : AppCompatActivity() {
    private lateinit var binding: ActivityAllProductsBinding
    private lateinit var productList: MutableList<ItemModel>
    private lateinit var allProducts: MutableList<ItemModel>
    private lateinit var productAdapter: ProductAdapter
    private val productApiService: API = RetrofitInstance.api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initProducts()
        initBottomMenu()
        initSwipeToRefresh()
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Refresh the products when user pulls down
            refreshProducts()
        }
    }

    private fun refreshProducts() {
        binding.swipeRefreshLayout.isRefreshing = true
        productApiService.getProducts().enqueue(object : Callback<MutableList<ItemModel>> {
            override fun onResponse(
                call: Call<MutableList<ItemModel>>,
                response: Response<MutableList<ItemModel>>
            ) {
                if (response.isSuccessful) {
                    allProducts.clear()
                    allProducts.addAll(response.body()!!)
                    productAdapter.notifyDataSetChanged()
                    binding.progressBarProducts.visibility = View.GONE
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<MutableList<ItemModel>>, t: Throwable) {
                Log.e("MainActivity", "onFailure: ${t.message}")
                binding.swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun initBottomMenu() {
        binding.bottomNavigation.selectedItemId = R.id.nav_explore
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@AllProducts, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_explore -> {
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this@AllProducts, CartActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this@AllProducts, OrderHistoryActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_account -> {
                    startActivity(Intent(this@AllProducts, CustomerProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
    private fun initProducts(){
        val gridLayoutManger = GridLayoutManager(this@AllProducts,2)
        binding.viewAllProducts.layoutManager = gridLayoutManger
        productList = mutableListOf()
        allProducts = mutableListOf()
        productAdapter = ProductAdapter(this@AllProducts, productList)
        binding.viewAllProducts.adapter = productAdapter
        binding.progressBarProducts.visibility = View.VISIBLE

        val selectedCategory = intent.getStringExtra("selectedCategory")

        productApiService.getProducts().enqueue(object : Callback<MutableList<ItemModel>> {
            override fun onResponse(
                call: Call<MutableList<ItemModel>>,
                response: Response<MutableList<ItemModel>>
            ) {
                if (response.isSuccessful) {
                    allProducts.clear()
                    allProducts.addAll(response.body()!!)

                    productList.clear()
                    if (selectedCategory != null) {
                        val filteredProducts = allProducts.filter { product ->
                            product.category.lowercase() == selectedCategory.lowercase()
                        }
                        productList.addAll(filteredProducts)
                    } else {
                        productList.addAll(allProducts)
                    }

                    productAdapter.notifyDataSetChanged()
                    binding.progressBarProducts.visibility = View.GONE

                    if (selectedCategory != null) {
                        binding.searchView.setQuery(selectedCategory, false)
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<ItemModel>>, t: Throwable) {
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })

        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    productList.clear()
                    productList.addAll(allProducts)
                    productAdapter.notifyDataSetChanged()
                } else {
                    searchList(newText)
                }
                return true
            }
        }

        binding.searchView.setOnQueryTextListener(listener)
        binding.searchView.setOnCloseListener {
            binding.searchView.setQuery("", false)
            false
        }
    }

    fun searchList(text: String){
        val searchList = ArrayList<ItemModel>()
        for (dataClass in allProducts) {
            val name = dataClass.name?.lowercase() ?: ""
            val category = dataClass.category?.lowercase() ?: ""
            val detail = dataClass.detail?.lowercase() ?: ""
            val tags = dataClass.tags.map { it.lowercase() }

            if (name.contains(text.lowercase()) ||
                category.contains(text.lowercase()) ||
                detail.contains(text.lowercase()) ||
                tags.any { it.contains(text.lowercase()) }) {
                searchList.add(dataClass)
            }
        }
        productAdapter.searchDataList(searchList)
    }
}