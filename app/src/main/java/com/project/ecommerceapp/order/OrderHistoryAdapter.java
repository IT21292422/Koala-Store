package com.project.ecommerceapp.order;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ecommerceapp.R;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private final ArrayList<Order> orders;

    public OrderHistoryAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.nameView.setText(order.getId());
        holder.addressView.setText(order.getAddress());
        holder.dateView.setText(order.getDate().substring(0, 10));
        holder.statusView.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameView, addressView, statusView, dateView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.tvItemName);
            addressView = itemView.findViewById(R.id.tvAddress);
            statusView = itemView.findViewById(R.id.tvStatus);
            dateView = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), OrderStatusActivity.class);
            Order order = orders.get(getAdapterPosition());
            intent.putExtra("status", order.getStatus());
            intent.putExtra("vendorId", order.getVendorId());
            intent.putParcelableArrayListExtra("order_items", order.getOrderItems());
            view.getContext().startActivity(intent);
        }

    }

}