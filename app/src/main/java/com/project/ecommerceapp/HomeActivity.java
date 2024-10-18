package com.project.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ecommerceapp.order.OrderHistoryActivity;
import com.project.ecommerceapp.order.OrderStatusActivity;
import com.project.ecommerceapp.profile.CustomerProfileActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btn_view_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CustomerProfileActivity.class));
            }
        });

        findViewById(R.id.btn_order_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
            }
        });
    }

}