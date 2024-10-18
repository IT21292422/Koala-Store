package com.project.ecommerceapp.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.ecommerceapp.EcommerceAPI;
import com.project.ecommerceapp.R;
import com.project.ecommerceapp.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileActivity.this.finish();
            }
        });

        EditText emailView = findViewById(R.id.email);
        EditText firstNameView = findViewById(R.id.fName);
        EditText lastNameView = findViewById(R.id.lName);

        emailView.setText(getIntent().getStringExtra("email"));
        firstNameView.setText(getIntent().getStringExtra("firstName"));
        lastNameView.setText(getIntent().getStringExtra("lastName"));

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser(emailView, firstNameView, lastNameView);
            }
        });
    }


    private void editUser(EditText emailView, EditText firstNameView, EditText lastNameView) {

        String email = emailView.getText().toString().trim();
        String firstName = firstNameView.getText().toString().trim();
        String lastName = lastNameView.getText().toString().trim();

        if (email.isEmpty()) {
            emailView.setError(getString(R.string.value_empty_error, "Email"));
            emailView.requestFocus();
            return;
        }
        if (firstName.isEmpty()) {
            firstNameView.setError(getString(R.string.value_empty_error, "First name"));
            firstNameView.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            lastNameView.setError(getString(R.string.value_empty_error, "Last name"));
            lastNameView.requestFocus();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                EcommerceAPI ecommerceAPI = new EcommerceAPI(EditProfileActivity.this);
                String userId = ecommerceAPI.getUserId();
                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("Email", email);
                    requestBody.put("FirstName", firstName);
                    requestBody.put("LastName", lastName);

                    Utils.showToastOnUiThread(EditProfileActivity.this, "Saving new account info...", Toast.LENGTH_LONG);

                    String response = ecommerceAPI.doPutRequest(ecommerceAPI.MAIN_URL + "user/"+userId, requestBody);
                    if (response != null) {
                        Utils.showToastOnUiThread(EditProfileActivity.this, "New info saved", Toast.LENGTH_LONG);
                    }
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}