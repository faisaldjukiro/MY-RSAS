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
import com.example.ovo.view.MainActivity;

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

        if (isLoggedIn()) {
            startMainActivity();
            finish();
        }
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

                if (username.isEmpty() || password.isEmpty()) {
                    showToast("Username dan password harus diisi");
                    return;
                }

                login(username, password);
            }
        });
    }
    private void login(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);
        Call<LoginResponse> call = apiService.login(username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    String kd_peg = loginResponse.getUserData().getKd_peg();
                    String token = loginResponse.getToken();
                    String namalengkap = loginResponse.getUserData().getNama();
                    showToast(namalengkap + " " +  "Login berhasil");
                    saveLoginData(kd_peg, token, namalengkap);
                    startMainActivity();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            JSONObject errorResponse = new JSONObject(response.errorBody().string());
                            String errorMessage = errorResponse.optString("message", "Terjadi kesalahan");
                            showToast(errorMessage);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showToast("Login gagal. Periksa koneksi internet Anda.");
                Log.e("LoginActivity", "Login failed", t);
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveLoginData(String kd_peg, String token, String namalengkap) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.login_status_key), true);
        editor.putString(getString(R.string.kd_peg_key), kd_peg);
        editor.putString(getString(R.string.token_key), token);
        editor.putString(getString(R.string.nama_lengkap_key), namalengkap);
        editor.apply();
    }

    private boolean isLoggedIn() {
        return sharedPref.getBoolean(getString(R.string.login_status_key), false);
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        String kd_peg = sharedPref.getString(getString(R.string.kd_peg_key), "");
        String token = sharedPref.getString(getString(R.string.token_key), "");
        String nama_lengkap = sharedPref.getString(getString(R.string.nama_lengkap_key), "");
        intent.putExtra("kd_peg", kd_peg);
        intent.putExtra("token", token);
        intent.putExtra("nama", nama_lengkap);
        startActivity(intent);
    }

    public void lihatButtonClicked(View view) {
        Intent intent = new Intent(this, CekpegActivity.class);
        startActivity(intent);
    }
}
