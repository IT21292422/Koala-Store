package com.project.ecommerceapp.Model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("Id")
    val id: String,

    @SerializedName("Email")
    val email: String,

    @SerializedName("Password")
    val password: String,

    @SerializedName("Role")
    val role: String,

    @SerializedName("FirstName")
    val firstName: String,

    @SerializedName("LastName")
    val lastName: String,

    @SerializedName("Detail")
    val detail: String,

    @SerializedName("Status")
    val status: Int,

    @SerializedName("CreatedAt")
    val createdAt: String,

    @SerializedName("UpdatedAt")
    val updatedAt: String
)
