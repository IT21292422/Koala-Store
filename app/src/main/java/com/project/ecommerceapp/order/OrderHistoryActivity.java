package com.project.ecommerceapp.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.ecommerceapp.EcommerceAPI;
import com.project.ecommerceapp.R;
import com.project.ecommerceapp.Utils;
import com.project.ecommerceapp.activity.AllProducts;
import com.project.ecommerceapp.activity.CartActivity;
import com.project.ecommerceapp.activity.MainActivity;
import com.project.ecommerceapp.profile.CustomerProfileActivity;
import com.project.ecommerceapp.profile.EditProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    private final ArrayList<Order> ordersArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        RecyclerView recyclerView = findViewById(R.id.orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderHistoryAdapter orderHistoryActivity = new OrderHistoryAdapter(ordersArrayList);
        recyclerView.setAdapter(orderHistoryActivity);
        getOrderHistory(orderHistoryActivity);
        initBottomMenu();
    }

    private void initBottomMenu() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.nav_orders);
        bottomNavigation.setOnItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(OrderHistoryActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_explore) {
                startActivity(new Intent(OrderHistoryActivity.this, AllProducts.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(OrderHistoryActivity.this, CartActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_orders) {
                return true;
            } else if (itemId == R.id.nav_account) {
                startActivity(new Intent(OrderHistoryActivity.this, CustomerProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void getOrderHistory(OrderHistoryAdapter adapter) {
        Utils.showToastOnUiThread(this, "Getting order history...", Toast.LENGTH_LONG);
        EcommerceAPI ecommerceAPI = new EcommerceAPI(this);
        String userId = ecommerceAPI.getUserId();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseBody = ecommerceAPI.doGetRequest(ecommerceAPI.MAIN_URL + "orders/" + userId);
                    Log.d("OrderHistory", "Response: " + responseBody);

                    JSONArray jsonArray = new JSONArray(responseBody);

                    if (jsonArray.length() == 0) {
                        Utils.showToastOnUiThread(OrderHistoryActivity.this, "No previous orders available", Toast.LENGTH_LONG);
                        return;
                    }

                    // Clear existing orders
                    ordersArrayList.clear();

                    // Process each order
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject orderInfoObject = jsonArray.getJSONObject(i);
                        Log.d("OrderHistory", "Processing order: " + orderInfoObject.getString("Id"));

                        String id = orderInfoObject.getString("Id");
                        String address = orderInfoObject.getString("Address");
                        String status = getOrderStatusByCode(String.valueOf(orderInfoObject.getInt("Status")));
                        String date = orderInfoObject.getString("CreatedAt");
                        JSONArray vendorsArray = orderInfoObject.getJSONArray("Vendors");
                        String vendorId = vendorsArray.length() > 0 ? vendorsArray.getString(0) : "";

                        // Process order items
                        ArrayList<OrderItem> orderItems = new ArrayList<>();
                        JSONArray orderItemsArray = orderInfoObject.getJSONArray("OrderItems");

                        Log.d("OrderHistory", "Order " + id + " has " + orderItemsArray.length() + " items");

                        for (int j = 0; j < orderItemsArray.length(); j++) {
                            JSONObject item = orderItemsArray.getJSONObject(j);
                            String itemId = item.getString("Id");
                            String productId = item.getString("ProductId");
                            String quantity = item.getString("Quantity");

                            OrderItem orderItem = new OrderItem(itemId, productId, quantity);
                            orderItems.add(orderItem);

                            Log.d("OrderHistory", "Added item: " + itemId + " to order: " + id);
                        }

                        Order order = new Order(id, address, date, status, vendorId, orderItems);
                        ordersArrayList.add(order);

                        Log.d("OrderHistory", "Added order: " + id + " with " + orderItems.size() + " items");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("OrderHistory", "Total orders loaded: " + ordersArrayList.size());
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (IOException | JSONException e) {
                    Log.e("OrderHistory", "Error loading orders", e);
                    final String errorMessage = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OrderHistoryActivity.this,
                                    "Error loading orders: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    private String getOrderStatusByCode(String code) {
        switch (code) {
            case "1":
                return "Dispatched";
            case "2":
                return "Partial";
            case "3":
                return "Delivered";
            case "4":
                return "Cancelled";
        }
        return "Processing";
    }

}