package com.example.ovo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ovo.api.ApiService;
import com.example.ovo.api.CheckKdPegawaiResponse;
import com.example.ovo.api.RetrofitInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CekpegActivity extends AppCompatActivity {
    private EditText kdPegawaiEditText;
    private Button checkButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cekpeg);

        kdPegawaiEditText = findViewById(R.id.kdPegawaiEditText);
        checkButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kdPegawai = kdPegawaiEditText.getText().toString();
                checkKdPegawai(kdPegawai);
            }
        });
    }

    private void checkKdPegawai(String kdPegawai) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<CheckKdPegawaiResponse> call = apiService.checkKdPegawai(kdPegawai);
        call.enqueue(new Callback<CheckKdPegawaiResponse>() {
            public void onResponse(Call<CheckKdPegawaiResponse> call, Response<CheckKdPegawaiResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    CheckKdPegawaiResponse checkResponse = response.body();
                    if (checkResponse.getStatus().equals("success")) {
                        String kdPegawai = checkResponse.getData().getKdPegawai();
                        String namaLengkap = checkResponse.getData().getNamaLengkap();
                        Toast.makeText(CekpegActivity.this, "" + namaLengkap, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CekpegActivity.this, BuatpassActivity.class);
                        intent.putExtra("kd_peg", kdPegawai);
                        intent.putExtra("nama", namaLengkap);
                        startActivity(intent);
                    } else {
                        showToast(checkResponse.getMessage());
                    }
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        String status = errorResponse.getString("status");
                        if (status.equals("error")) {
                            String errorMessage = errorResponse.getString("message");
                            showToast(errorMessage);
                        }
                    } catch (JSONException | IOException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckKdPegawaiResponse> call, Throwable t) {
                showToast("Koneksi gagal. Silakan coba lagi.");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
