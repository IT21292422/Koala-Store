package com.project.ecommerceapp.Model

import com.google.gson.annotations.SerializedName

data class SliderModel(
    @SerializedName("Id")
    val id: String,

    @SerializedName("ImageUrl")
    val imageUrl:String,

    @SerializedName("IsActive")
    val isActive:Boolean? = null,

    @SerializedName("Order")
    val order:Int? = null
){
    constructor(imageUrl: String) : this("", imageUrl, null, null)
}
