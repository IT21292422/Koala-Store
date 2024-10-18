package com.project.ecommerceapp.rate;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ecommerceapp.R;
import com.project.ecommerceapp.Utils;

public class RateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateActivity.this.finish();
            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishVendorReview();
            }
        });
    }

    private void publishVendorReview() {
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        EditText reviewView = findViewById(R.id.review);

        float rating = ratingBar.getRating();
        String review = reviewView.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, "Please select a star rating", Toast.LENGTH_SHORT).show();
            return;
        }
        if (review.isEmpty()) {
            reviewView.setError(getString(R.string.value_empty_error, "Review"));
            reviewView.requestFocus();
            return;
        }

        Utils.showToastOnUiThread(this, "Publishing vendor rating...", Toast.LENGTH_LONG);
    }

}