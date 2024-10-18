package com.project.ecommerceapp.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.ecommerceapp.EcommerceAPI;
import com.project.ecommerceapp.R;
import com.project.ecommerceapp.Utils;
import com.project.ecommerceapp.activity.AllProducts;
import com.project.ecommerceapp.activity.CartActivity;
import com.project.ecommerceapp.activity.MainActivity;
import com.project.ecommerceapp.order.OrderHistoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CustomerProfileActivity extends AppCompatActivity {
    private EcommerceAPI ecommerceAPI;
    private String userId;
    private String email, firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        ecommerceAPI = new EcommerceAPI(this);
        userId = ecommerceAPI.getUserId();

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email)) {
                    return;
                }
                Intent intent = new Intent(CustomerProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnDeactivate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeactivateAccountDialog();
            }
        });

        getUserAccountInfo();
        initBottomMenu();
    }

    private void initBottomMenu() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.nav_account);
        bottomNavigation.setOnItemSelectedListener(menuItem -> {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(CustomerProfileActivity.this, MainActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_explore) {
                    startActivity(new Intent(CustomerProfileActivity.this, AllProducts.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    startActivity(new Intent(CustomerProfileActivity.this, CartActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_orders) {
                    startActivity(new Intent(CustomerProfileActivity.this, OrderHistoryActivity.class));
                    finish();
                    return true;
                } else if (itemId == R.id.nav_account) {
                    return true;
                }
                return false;
        });
    }

    private void logout() {
        ecommerceAPI.setRegistered(false);
        ecommerceAPI.setLoggedIn(false);
        startActivity(new Intent(CustomerProfileActivity.this, RegisterActivity.class));
        finish();
    }

    private void getUserAccountInfo() {
        Utils.showToastOnUiThread(this, "Getting account info...", Toast.LENGTH_LONG);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String responseBody = ecommerceAPI.doGetRequest(ecommerceAPI.MAIN_URL + "user/"+userId);

                    JSONObject userInfo = new JSONObject(responseBody);
                    email = userInfo.getString("Email");
                    firstName = userInfo.getString("FirstName");
                    lastName = userInfo.getString("LastName");
                    String createdAt = userInfo.getString("CreatedAt");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView emailView = findViewById(R.id.tvEmail);
                            TextView nameView = findViewById(R.id.tvName);
                            TextView dateView = findViewById(R.id.tvCreationDate);

                            emailView.setText("Email : "+email);
                            nameView.setText("Name : "+firstName+" "+lastName);
                            dateView.setText("Created on : "+createdAt.substring(0, 10));
                        }
                    });

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void showDeactivateAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deactivate Account")
                .setMessage("Are you sure you want to deactivate this account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deactivateAccount();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void deactivateAccount() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("Status", 0);

                    Utils.showToastOnUiThread(CustomerProfileActivity.this, "Deactivating account...", Toast.LENGTH_LONG);
                    ecommerceAPI.doPutRequest(ecommerceAPI.MAIN_URL + "user/"+userId, requestBody);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            logout();
                        }
                    });
                }
                catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}