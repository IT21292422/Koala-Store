package com.project.ecommerceapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.ecommerceapp.Adapter.CartAdapter
import com.project.ecommerceapp.EcommerceAPI
import com.project.ecommerceapp.Model.ItemModel
import com.project.ecommerceapp.Model.OrderItem
import com.project.ecommerceapp.Model.OrderModel
import com.project.ecommerceapp.R
import com.project.ecommerceapp.databinding.ActivityCartBinding
import com.project.ecommerceapp.manager.CartManager
import com.project.ecommerceapp.network.API
import com.project.ecommerceapp.network.RetrofitInstance
import com.project.ecommerceapp.order.OrderHistoryActivity
import com.project.ecommerceapp.profile.CustomerProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import java.util.UUID

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartManager: CartManager
    private lateinit var ecommerceAPI: EcommerceAPI
    private lateinit var userId: String
    private val productApiService: API = RetrofitInstance.api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ecommerceAPI = EcommerceAPI(this)
        userId = ecommerceAPI.userId
        cartManager = CartManager(this)
        initCartList()
        calculateCart()
        handleEmptyCartVisibility()
        initBottomMenu()

        binding.checkoutBtn.setOnClickListener {
            showConfirmDialog(this)
//            val order = createOrder(userId)
//            sendOrder(order)
        }
    }

    private fun initBottomMenu() {
        binding.bottomNavigation.selectedItemId = R.id.nav_cart
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@CartActivity, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_explore -> {
                    startActivity(Intent(this@CartActivity, AllProducts::class.java))
                    finish()
                    true
                }
                R.id.nav_cart -> {
                    true
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this@CartActivity, OrderHistoryActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_account -> {
                    startActivity(Intent(this@CartActivity, CustomerProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun initCartList(){
        val cartItems = cartManager.fetchCartItems()

        binding.viewCart.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        cartAdapter = CartAdapter(cartItems, this,
            onQuantityChanged = { item->
                cartManager.updateQuantity(item.id, item.quantity)
                calculateCart()
                handleEmptyCartVisibility()
            },
            onItemRemoved = { item: ItemModel ->
                cartManager.removeItem(item)
                Toast.makeText(this, "${item.name} removed from cart", Toast.LENGTH_SHORT).show()
                calculateCart()
                handleEmptyCartVisibility()
            }
        )
        binding.viewCart.adapter = cartAdapter
    }

    private fun handleEmptyCartVisibility(){
        val cartItems = cartManager.fetchCartItems()
        with(binding){
            emptyText.visibility = if(cartItems.isEmpty()) View.VISIBLE else View.GONE
            cartScroll.visibility = if(cartItems.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun calculateCart() {
        val itemTotal = cartManager.getTotalPrice()
        val roundedTotal = String.format("%.2f", itemTotal)
        with(binding) {
            totalText.text = "Rs.$roundedTotal"
        }
    }

    private fun showConfirmDialog(context: Context) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val addressInput = EditText(context).apply {
            hint = "Enter your address"
        }
        val detailInput = EditText(context).apply {
            hint = "Enter any additional details"
        }

        layout.addView(addressInput)
        layout.addView(detailInput)

        // Build the AlertDialog
        AlertDialog.Builder(context)
            .setTitle("Confirm Details")
            .setView(layout)
            .setPositiveButton("Submit") { dialog, _ ->
                val address = addressInput.text.toString()
                val detail = detailInput.text.toString()
                if (address.isNotEmpty() && detail.isNotEmpty()) {
                    val order = createOrder(userId,address, detail)
                    sendOrder(order)
                } else {
                    Toast.makeText(context, "Address and Details cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun createOrder(userId: String, address: String, details: String): OrderModel {
        return OrderModel(
            status = 0,
            customerId = userId,
            address = address,
            orderItems = cartManager.fetchOrderItems(),
            payment = cartManager.getTotalPrice().toString(),
            paymentStatus = 1,
            cancellationNote = null,
            detail = details,
            vendors = cartManager.fetchCartItems().map { it.vendorId },
        )
    }

    private fun sendOrder(order: OrderModel) {
        val call = productApiService.createOrder(order)

        call.enqueue(object : Callback<OrderModel> {
            override fun onResponse(call: Call<OrderModel>, response: Response<OrderModel>) {
                if (response.isSuccessful) {
                    cartManager.clearCart()
                    Toast.makeText(this@CartActivity, "Order sent successfully", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "Order sent successfully: ${response.body()}")
                } else {
                    Log.e("MainActivity", "Failed to send order: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<OrderModel>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }
}