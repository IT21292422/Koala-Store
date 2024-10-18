package com.project.ecommerceapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.project.ecommerceapp.Model.ItemModel
import com.project.ecommerceapp.databinding.ViewholderCartBinding

class CartAdapter(private val cartItems: ArrayList<ItemModel>, context: Context, private val onQuantityChanged: (ItemModel) -> Unit, private val onItemRemoved: (ItemModel) -> Unit):
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val binding: ViewholderCartBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val item = cartItems[position]
        holder.binding. titleText.text = item.name
        holder.binding.unitCostText.text="Rs.${item.price}"
        holder.binding.totalCostText.text = "Rs.${item.quantity*item.price.toInt()}"
        holder.binding.numberItemText.text=item.quantity.toString()

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.pic)

        holder.binding.plusCartBtn.setOnClickListener {
            item.quantity++
            onQuantityChanged(item)
            holder.binding.numberItemText.text=item.quantity.toString()
            holder.binding.totalCostText.text = "Rs.${item.quantity*item.price.toInt()}"
        }

        holder.binding.minusCartBtn.setOnClickListener {
            if(item.quantity > 1){
                item.quantity--
                onQuantityChanged(item)
                holder.binding.numberItemText.text=item.quantity.toString()
                holder.binding.totalCostText.text = "Rs.${item.quantity * item.price.toInt()}"
            }else{
                cartItems.removeAt(position)
                notifyItemRemoved(position)
                onItemRemoved(item)
                notifyItemRangeChanged(position, cartItems.size)
            }
        }
    }
}