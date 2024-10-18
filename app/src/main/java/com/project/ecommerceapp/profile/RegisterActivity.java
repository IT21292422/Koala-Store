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

public class RegisterActivity extends AppCompatActivity {

    private EcommerceAPI ecommerceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initActivity();
    }

    private void initActivity() {

        if (!Utils.hasInternetConnection(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection")
                    .setMessage("An internet connection is required to open the app")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            initActivity();
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
            return;
        }

        ecommerceAPI = new EcommerceAPI(this);
        if (ecommerceAPI.isLoggedIn()) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
            return;
        } else if (ecommerceAPI.isRegistered()) {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
            return;
        }

        EditText emailView = findViewById(R.id.email);
        EditText firstNameView = findViewById(R.id.fName);
        EditText lastNameView = findViewById(R.id.lName);
        EditText passwordView = findViewById(R.id.password);
        EditText confirmPasswordView = findViewById(R.id.rePassword);

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(emailView, firstNameView, lastNameView, passwordView, confirmPasswordView);
            }
        });

        findViewById(R.id.switchToLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                RegisterActivity.this.finish();
            }
        });
    }

    private void registerUser(EditText emailView, EditText firstNameView, EditText lastNameView,
                              EditText passwordView, EditText confirmPasswordView) {

        String email = emailView.getText().toString().trim();
        String firstName = firstNameView.getText().toString().trim();
        String lastName = lastNameView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();
        String confirmPassword = confirmPasswordView.getText().toString().trim();

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
        if (password.isEmpty()) {
            passwordView.setError(getString(R.string.value_empty_error, "Password"));
            passwordView.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordView.setError("Passwords does not match");
            confirmPasswordView.requestFocus();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("Email", email);
                    requestBody.put("Password", password);
                    requestBody.put("FirstName", firstName);
                    requestBody.put("LastName", lastName);
                    requestBody.put("Role", "User");
                    requestBody.put("Detail", "Empty");

                    Utils.showToastOnUiThread(RegisterActivity.this, "Please wait, Registration is in progress...", Toast.LENGTH_LONG);
                    ecommerceAPI.doPostRequest(ecommerceAPI.MAIN_URL + "auth/register", requestBody);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ecommerceAPI.setRegistered(true);
                            showRegisteredSuccessDialog();
                        }
                    });

                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void showRegisteredSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Successful")
                .setMessage(R.string.registration_complete_msg)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        RegisterActivity.this.finish();
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        RegisterActivity.this.finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

}