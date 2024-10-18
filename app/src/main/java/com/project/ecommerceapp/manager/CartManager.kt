package com.project.ecommerceapp.manager

import android.content.Context
import com.project.ecommerceapp.Model.ItemModel
import com.project.ecommerceapp.Model.OrderItem
import com.project.ecommerceapp.utils.SharedPreferencesHelper


class CartManager(context: Context) {
    private val sharedPreferencesHelper = SharedPreferencesHelper(context)
    private var cartItems: ArrayList<ItemModel> = sharedPreferencesHelper.getCartItems()

    fun addItem(item: ItemModel){
        val existingItem = cartItems.find{ it.id == item.id}
        if(existingItem != null){
            existingItem.quantity += item.quantity
        }else{
            cartItems.add(item)
        }
        sharedPreferencesHelper.saveCartItems(cartItems)
    }

    fun removeItem(item: ItemModel){
        cartItems.removeIf { it.id == item.id }
        sharedPreferencesHelper.saveCartItems(cartItems)
    }

    fun updateQuantity(productId: String, newQuantity: Int){
        val item = cartItems.find{ it.id == productId}
        item?.let{
            it.quantity = newQuantity
        }
        sharedPreferencesHelper.saveCartItems(cartItems)
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.price.toDouble() * it.quantity.toDouble() }
    }

    fun fetchCartItems(): ArrayList<ItemModel>{
        return cartItems
    }

    fun mapItemToOrder(item: ItemModel): OrderItem {
        return OrderItem(
                productId = item.id,
                quantity = item.quantity,
                status = item.status,
                detail = item.vendorId
        )
    }

    fun fetchOrderItems(): ArrayList<OrderItem> {
        return ArrayList(cartItems.map { mapItemToOrder(it) })
    }

    fun clearCart() {
        cartItems.clear()
        sharedPreferencesHelper.clearCart()
    }
}