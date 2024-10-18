package com.project.ecommerceapp.Model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class OrderModel(
    @SerializedName("Id")
    val id: String? = null,

    @SerializedName("Status")
    val status: Int,

    @SerializedName("CustomerId")
    val customerId: String,

    @SerializedName("Address")
    val address: String,

    @SerializedName("OrderItems")
    val orderItems: ArrayList<OrderItem>,

    @SerializedName("Payment")
    val payment: String,

    @SerializedName("PaymentStatus")
    val paymentStatus: Int,

    @SerializedName("CancellationNote")
    val cancellationNote: String? = null,

    @SerializedName("Detail")
    val detail: String? = null,

    @SerializedName("Vendors")
    val vendors: List<String>,

    @SerializedName("CreatedAt")
    val createdAt: Date?=null,

    @SerializedName("UpdatedAt")
    val updatedAt: Date?=null
)

data class OrderItem(
    @SerializedName("Id")
    val id: String?=null,

    @SerializedName("ProductId")
    val productId: String,

    @SerializedName("Quantity")
    val quantity: Int,

    @SerializedName("Status")
    val status: Int,

    @SerializedName("Detail")
    val detail: String? = null,

    @SerializedName("CreatedAt")
    val createdAt: Date? = null,

    @SerializedName("UpdatedAt")
    val updatedAt: Date? = null
)