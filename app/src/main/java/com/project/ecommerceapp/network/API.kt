package com.project.ecommerceapp.network

import com.project.ecommerceapp.Model.ItemModel
import com.project.ecommerceapp.Model.OrderModel
import com.project.ecommerceapp.Model.SliderModel
import com.project.ecommerceapp.Model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface API {
    @GET("api/products")
    fun getProducts(): Call<MutableList<ItemModel>>

    @GET("api/products/{id}")
    fun getProductById(@Path("id") id:String): Call<ItemModel>

    @GET("/api/carousels")
    fun getBanners(): Call<MutableList<SliderModel>>

    @GET("api/user/{id}")
    fun getUserById(@Path("id") id:String): Call<UserModel>

    @POST("api/orders")
    fun createOrder(@Body order: OrderModel): Call<OrderModel>
}