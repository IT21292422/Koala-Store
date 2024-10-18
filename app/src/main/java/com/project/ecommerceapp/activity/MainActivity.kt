package com.project.ecommerceapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.project.ecommerceapp.Adapter.ProductAdapter
import com.project.ecommerceapp.Adapter.SliderAdapter
import com.project.ecommerceapp.EcommerceAPI
import com.project.ecommerceapp.Model.ItemModel
import com.project.ecommerceapp.Model.SliderModel
import com.project.ecommerceapp.Model.UserModel
import com.project.ecommerceapp.R
import com.project.ecommerceapp.databinding.ActivityMainBinding
import com.project.ecommerceapp.network.API
import com.project.ecommerceapp.network.RetrofitInstance
import com.project.ecommerceapp.order.OrderHistoryActivity
import com.project.ecommerceapp.profile.CustomerProfileActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sliderList: ArrayList<SliderModel>
    private lateinit var productList: MutableList<ItemModel>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var sliderAdapter: SliderAdapter
    private val productApiService: API = RetrofitInstance.api
    private lateinit var ecommerceAPI: EcommerceAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ecommerceAPI = EcommerceAPI(this)
        val userId = ecommerceAPI.userId
        getUsername(userId)
        initBanner()
        initPopular()
        initBottomMenu()

        binding.electronicBtn.setOnClickListener {
            openProductsWithCategory("electronic")
        }

        binding.beautyBtn.setOnClickListener {
            openProductsWithCategory("beauty")
        }

        binding.clothBtn.setOnClickListener {
            openProductsWithCategory("clothing")
        }

        binding.foodBtn.setOnClickListener {
            openProductsWithCategory("food")
        }

        binding.seeAllBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, AllProducts::class.java))
        }
    }

    private fun openProductsWithCategory(category: String) {
        val intent = Intent(this, AllProducts::class.java)
        intent.putExtra("selectedCategory", category)
        startActivity(intent)
    }

    private fun getUsername(userId:String) {
        productApiService.getUserById(userId).enqueue(object : Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) {
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    if (userInfo != null) {
                        binding.nameTxt.text = "${userInfo.firstName} ${userInfo.lastName}"
                    }
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
        }

    private fun initBottomMenu() {
        binding.bottomNavigation.selectedItemId = R.id.nav_home
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_explore -> {
                    startActivity(Intent(this@MainActivity, AllProducts::class.java))
                    finish()
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this@MainActivity, CartActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this@MainActivity, OrderHistoryActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_account -> {
                    startActivity(Intent(this@MainActivity, CustomerProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }


    private fun initBanner(){
        binding.progressBarBanner.visibility = View.VISIBLE
        productApiService.getBanners().enqueue(object : Callback<MutableList<SliderModel>> {
            override fun onResponse(
                call: Call<MutableList<SliderModel>>,
                response: Response<MutableList<SliderModel>>
            ) {
                if (response.isSuccessful) {
                    val activeBanners = response.body()?.filter { it.isActive == true } ?: emptyList()
                    if (activeBanners.isNotEmpty()) {
                        banners(activeBanners)
                    }
                    binding.progressBarBanner.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MutableList<SliderModel>>, t: Throwable) {
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun banners(images: List<SliderModel>){
        binding.viewPagerSlider.adapter= SliderAdapter(images, binding.viewPagerSlider)
        binding.viewPagerSlider.clipToPadding=false
        binding.viewPagerSlider.clipChildren=false
        binding.viewPagerSlider.offscreenPageLimit=3
        binding.viewPagerSlider.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER
        val compositePageTransformer = CompositePageTransformer().apply{
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer)
        if(images.size>1){
            binding.dotsIndicator.visibility = View.VISIBLE
            binding.dotsIndicator.attachTo(binding.viewPagerSlider)
        }
    }

    private fun initPopular(){
        val GridLayoutManger = GridLayoutManager(this@MainActivity,2)
        binding.viewPopular.layoutManager = GridLayoutManger
        productList = mutableListOf()
        productAdapter = ProductAdapter(this@MainActivity, productList)
        binding.viewPopular.adapter = productAdapter
        binding.progressBarPopular.visibility = View.VISIBLE
        productApiService.getProducts().enqueue(object : Callback<MutableList<ItemModel>> {
            override fun onResponse(
                call: Call<MutableList<ItemModel>>,
                response: Response<MutableList<ItemModel>>
            ) {
                if (response.isSuccessful) {
                    productList.clear()
                    response.body()?.let { allProducts ->
                        val latestProducts = allProducts.takeLast(6)
                        productList.addAll(latestProducts)
                    }
                    productAdapter.notifyDataSetChanged()
                    binding.progressBarPopular.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MutableList<ItemModel>>, t: Throwable) {
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}