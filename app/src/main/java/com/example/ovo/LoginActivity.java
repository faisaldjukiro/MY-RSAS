package com.example.ovo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ovo.api.ApiService;
import com.example.ovo.api.LoginResponse;
import com.example.ovo.api.RetrofitInstance;
import com.example.ovo.view.LottieActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private ApiService apiService;
    private ProgressBar progressBar;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        apiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                login(username, password);
            }
        });

        // Check if user is already logged in
        if (isLoggedIn()) {
            startNextActivity();
        }
    }

    private void login(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);
        Call<LoginResponse> call = apiService.login(username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    // Save login status
                    saveLoginStatus(true);
                    LoginResponse loginResponse = response.body();
                    String message = loginResponse.getUserData().getNama();
                    showToast(message);
                    startNextActivity();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            JSONObject errorResponse = new JSONObject(response.errorBody().string());
                            String status = errorResponse.getString("status");
                            if (status.equals("error")) {
                                String errorMessage = errorResponse.getString("message");
                                showToast(errorMessage);
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showToast("Login failed");
                Log.e("LoginActivity", "Login failed", t);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void startNextActivity() {
        Intent intent = new Intent(LoginActivity.this, LottieActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.login_status_key), isLoggedIn);
        editor.apply();
    }

    private boolean isLoggedIn() {
        return sharedPref.getBoolean(getString(R.string.login_status_key), false);
    }

    public void lihatButtonClicked(View view) {
        Intent intent = new Intent(this, CekpegActivity.class);
        startActivity(intent);
    }
}