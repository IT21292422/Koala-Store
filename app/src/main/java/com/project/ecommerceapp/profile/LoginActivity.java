package com.project.ecommerceapp.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.ecommerceapp.EcommerceAPI;
import com.project.ecommerceapp.HomeActivity;
import com.project.ecommerceapp.R;
import com.project.ecommerceapp.Utils;
import com.project.ecommerceapp.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailView = findViewById(R.id.email);
        EditText passwordView = findViewById(R.id.password);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(emailView, passwordView);
            }
        });

        findViewById(R.id.switchToRegisterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                LoginActivity.this.finish();
            }
        });
    }

    private void loginUser(EditText emailView, EditText passwordView) {

        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();

        if (email.isEmpty()) {
            emailView.setError(getString(R.string.value_empty_error, "Email"));
            emailView.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordView.setError(getString(R.string.value_empty_error, "Password"));
            passwordView.requestFocus();
            return;
        }

        EcommerceAPI ecommerceAPI = new EcommerceAPI(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("Email", email);
                    requestBody.put("Password", password);

                    Utils.showToastOnUiThread(LoginActivity.this, "Logging in to your account...", Toast.LENGTH_LONG);
                    String response = ecommerceAPI.doPostRequest(ecommerceAPI.MAIN_URL + "auth/login", requestBody);

                    if (response == null) {
                        Utils.showToastOnUiThread(LoginActivity.this, "Wrong email or password. Please try again.", Toast.LENGTH_LONG);
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("message")) {
                        String message = jsonObject.getString("message");
                        if (message.equals("User not found.")) {
                            Utils.showToastOnUiThread(LoginActivity.this, "Wrong email or password. Please try again.", Toast.LENGTH_LONG);
                        }
                    }
                    else {
                        JSONObject userObject = jsonObject.getJSONObject("user");
                        String userId = userObject.getString("Id");
                        String status = userObject.getString("Status");

                        ecommerceAPI.setLoggedIn(true);
                        ecommerceAPI.saveUserId(userId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status.equals("0")) {
                                    showAccountNotActivatedDialog();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                }
                            }
                        });
                    }

                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void showAccountNotActivatedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Successful")
                .setMessage(R.string.unactivated_account_msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

}