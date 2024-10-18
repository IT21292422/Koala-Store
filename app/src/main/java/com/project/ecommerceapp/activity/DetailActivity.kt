package com.project.ecommerceapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.ecommerceapp.Adapter.SliderAdapter
import com.project.ecommerceapp.Model.ItemModel
import com.project.ecommerceapp.Model.SliderModel
import com.project.ecommerceapp.R
import com.project.ecommerceapp.databinding.ActivityDetailBinding
import com.project.ecommerceapp.manager.CartManager
import com.project.ecommerceapp.network.API
import com.project.ecommerceapp.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemModel
    private lateinit var cartManager: CartManager
    private val productApiService: API = RetrofitInstance.api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cartManager = CartManager(this)
        getDetails()

    }

    private fun getDetails(){
        val productId: String = intent.getStringExtra("id").toString()
        binding.progressBarDetail.visibility = View.VISIBLE
        productApiService.getProductById(productId).enqueue(object : Callback<ItemModel> {
            override fun onResponse(
                call: Call<ItemModel>,
                response: Response<ItemModel>
            ) {
                if (response.isSuccessful) {
                    item = response.body()!!
                    binding.progressBarDetail.visibility = View.GONE
                    binding.titleText.text = item.name
                    binding.priceTxt.text = "Rs. ${item.price}"
                    binding.descriptionText.text = item.detail

                    banners()
                }
            }

            override fun onFailure(call: Call<ItemModel>, t: Throwable) {
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })

        binding.backBtn.setOnClickListener{finish()}

        binding.cartBtn.setOnClickListener {
            if (::item.isInitialized) {
                cartManager.addItem(item)
                Toast.makeText(this, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@DetailActivity, CartActivity::class.java))
            } else {
                Toast.makeText(this, "Item details are not loaded yet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun banners(){
        val sliderItems = ArrayList<SliderModel>()
        sliderItems.add(SliderModel(item.imageUrl))
        binding.slider.adapter= SliderAdapter(sliderItems,binding.slider)
        binding.slider.clipToPadding=true
        binding.slider.clipChildren=true
        binding.slider.offscreenPageLimit=1

        if(sliderItems.size>1){
            binding.dotsIndicator.visibility = View.VISIBLE
            binding.dotsIndicator.attachTo(binding.slider)
        }
    }
}