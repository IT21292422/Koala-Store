package com.project.ecommerceapp.Model

import com.google.gson.annotations.SerializedName

data class ItemModel(
    @SerializedName("Id")
    val id: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Category")
    val category: String,

    @SerializedName("VendorId")
    val vendorId: String,

    @SerializedName("Status")
    val status: Int,

    @SerializedName("Price")
    private val _price: String,

    @SerializedName("Quantity")
    var quantity: Int,

    @SerializedName("ImageUrl")
    val imageUrl: String,

    @SerializedName("Detail")
    val detail: String,

    @SerializedName("Tags")
    val tags: List<String>,

    @SerializedName("CreatedAt")
    val createdAt: String,

    @SerializedName("UpdatedAt")
    val updatedAt: String
){
    val price: Int
        get() = try {
            _price.toInt()
        } catch (e: NumberFormatException) {
            0
        }
}
