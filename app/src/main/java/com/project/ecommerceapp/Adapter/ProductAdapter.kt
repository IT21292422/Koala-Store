package com.project.ecommerceapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.project.ecommerceapp.Model.ItemModel
import com.project.ecommerceapp.R
import com.project.ecommerceapp.activity.DetailActivity

class ProductAdapter(private val context: Context, private var items: MutableList<ItemModel>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_product,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    fun searchDataList(searchList: MutableList<ItemModel>){
        items = searchList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleText.text=items[position].name
        holder.priceText.text="Rs."+items[position].price.toString()

        val imageUrl = items[position].imageUrl
        val requestOptions = RequestOptions().transform(CenterCrop())

        if(imageUrl!=null){
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(holder.pic)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("id", items[position].id)
            holder.itemView.context.startActivity(intent)
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var titleText: TextView
        var priceText: TextView
        var pic: ImageView

        init{
            titleText = itemView.findViewById(R.id.titleText)
            priceText = itemView.findViewById(R.id.priceText)
            pic = itemView.findViewById(R.id.pic)
        }

    }
}