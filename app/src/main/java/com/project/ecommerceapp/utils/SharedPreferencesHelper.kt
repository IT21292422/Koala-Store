package com.project.ecommerceapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.ecommerceapp.Model.ItemModel


class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    companion object {
        const val CART_ITEMS_KEY = "cart_items"
    }

    // Save cart items to SharedPreferences
    fun saveCartItems(cartItems: ArrayList<ItemModel>) {
        val json = gson.toJson(cartItems)
        editor.putString(CART_ITEMS_KEY, json)
        editor.apply()
    }

    // Retrieve cart items from SharedPreferences
    fun getCartItems(): ArrayList<ItemModel> {
        val json = sharedPreferences.getString(CART_ITEMS_KEY, null)
        val type = object : TypeToken<ArrayList<ItemModel>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    // Clear cart
    fun clearCart() {
        editor.remove(CART_ITEMS_KEY)
        editor.apply()
    }
}