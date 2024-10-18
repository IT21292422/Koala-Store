package com.project.ecommerceapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EcommerceAPI {

    public final String MAIN_URL = "https://e4d4-112-135-65-241.ngrok-free.app/api/";
    private final OkHttpClient client = new OkHttpClient();
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final SharedPreferences preferences;
    private final Activity activity;


    public EcommerceAPI(Activity activity) {
        this.activity = activity;
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("logged_in", loggedIn);
        editor.apply();
    }

    public void setRegistered(boolean registered) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("registered", registered);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("logged_in", false);
    }

    public boolean isRegistered() {
        return preferences.getBoolean("registered", false);
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", userId);
        editor.apply();
    }

    public String getUserId() {
        return preferences.getString("user_id", "");
    }

    public String doPostRequest(String url, JSONObject jsonObject) throws IOException {
        String jsonBody = jsonObject.toString();
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
//                throw new IOException("Unexpected code " + response);
                Utils.showToastOnUiThread(activity, R.string.request_error, Toast.LENGTH_SHORT);
                return null;
            }
        }
    }

    public String doPutRequest(String url, JSONObject jsonObject) throws IOException {
        String jsonBody = jsonObject.toString();
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.d("TAG", "doPutRequest: "+response.body().string());
                Utils.showToastOnUiThread(activity, R.string.request_error, Toast.LENGTH_SHORT);
                return null;
            }
        }
    }

    public String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Utils.showToastOnUiThread(activity, R.string.request_error, Toast.LENGTH_SHORT);
                return null;
            }
        }
    }

}