package com.project.ecommerceapp.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ecommerceapp.R;
import com.project.ecommerceapp.profile.CustomerProfileActivity;
import com.project.ecommerceapp.rate.RateActivity;
import com.project.ecommerceapp.rate.VendorRatingActivity;

import java.util.ArrayList;

public class OrderStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        ArrayList<OrderItem> orderItems = getIntent().getParcelableArrayListExtra("order_items");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(orderItems);
        recyclerView.setAdapter(orderStatusAdapter);

        String status = getIntent().getStringExtra("status");
        TextView statusView = findViewById(R.id.tvOrderStatus);
        statusView.setText("Status : " + status);

        TextView totalView = findViewById(R.id.tvTotal);
        totalView.setText("Total : " + orderItems.size());

        findViewById(R.id.btnViewRatings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderStatusActivity.this, VendorRatingActivity.class);
                String vendorId = getIntent().getStringExtra("vendorId");
                intent.putExtra("vendorId", vendorId);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnRateVendor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderStatusActivity.this, RateActivity.class));
            }
        });
    }

}