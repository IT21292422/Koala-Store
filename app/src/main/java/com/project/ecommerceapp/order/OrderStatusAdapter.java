package com.project.ecommerceapp.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ecommerceapp.R;

import java.util.ArrayList;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {

    private final ArrayList<OrderItem> orderItems;

    public OrderStatusAdapter(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_status_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.nameView.setText(orderItem.getProductId());
        holder.quantityView.setText(orderItem.getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView, quantityView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.tvItem);
            quantityView = itemView.findViewById(R.id.tvNumOfItems);
        }

    }

}