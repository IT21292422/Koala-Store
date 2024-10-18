package com.project.ecommerceapp.rate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ecommerceapp.EcommerceAPI;
import com.project.ecommerceapp.R;
import com.project.ecommerceapp.Utils;
import com.project.ecommerceapp.order.OrderHistoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class VendorRatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_ratings);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VendorRatingActivity.this.onBackPressed();
            }
        });

        getVendorRating();
    }

    private void getVendorRating() {
        Utils.showToastOnUiThread(this, "Getting vendor rating...", Toast.LENGTH_LONG);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    EcommerceAPI ecommerceAPI = new EcommerceAPI(VendorRatingActivity.this);
                    String vendorId = getIntent().getStringExtra("vendorId");
                    String responseBody = ecommerceAPI.doGetRequest(ecommerceAPI.MAIN_URL +"user/vendor/"+vendorId+"/rating");

                    JSONObject userInfo = new JSONObject(responseBody);
                    Log.d("TAG", "userInfo: "+userInfo);
                    if (userInfo.has("rating")) {
                        String rating = userInfo.getString("rating");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView ratingView = findViewById(R.id.ratingView);
                                ratingView.setText(rating);

                                RatingBar ratingBar = findViewById(R.id.ratingBar);
                                ratingBar.setRating(Float.parseFloat(rating));
                            }
                        });
                    }

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}