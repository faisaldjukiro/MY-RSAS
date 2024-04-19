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
import com.example.ovo.api.RetrofitInstance;
import com.example.ovo.api.UpdatePasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuatpassActivity extends AppCompatActivity {
    private EditText namaLengkapEditText, kdpegawai, newPasswordEditText, password1, password2;
    private Button updateButton;
    private String kodePegawai;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buatpass);

        namaLengkapEditText = findViewById(R.id.nama_lengkap);
        kdpegawai = findViewById(R.id.kdPegawaiEditText);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        updateButton = findViewById(R.id.updateButton);
        progressBar = findViewById(R.id.progressBar);


        String namaPegawai = getIntent().getStringExtra("nama");
        kodePegawai = getIntent().getStringExtra("kd_peg");

        namaLengkapEditText.setText(namaPegawai);
        kdpegawai.setText(kodePegawai);
        namaLengkapEditText.setEnabled(false);
        namaLengkapEditText.setFocusable(false);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = password1.getText().toString();
                String repeatPassword = password2.getText().toString();

                if (!newPassword.equals(repeatPassword)) {
                    showToast("Password tidak cocok");
                    return;
                }

                if (newPassword.isEmpty() || repeatPassword.isEmpty()) {
                    showToast("Harap isi kedua password.");
                    return;
                }
                if (!newPassword.equals(repeatPassword)) {
                    showToast("Password tidak cocok. Silakan masukkan password yang sama.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                updatePassword(kodePegawai, newPassword);
            }
        });
    }

    private void updatePassword(String kdPegawai, String newPassword) {
        ApiService apiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<UpdatePasswordResponse> call = apiService.updatePassword(kdPegawai, newPassword);
        call.enqueue(new Callback<UpdatePasswordResponse>() {
            @Override
            public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    UpdatePasswordResponse updateResponse = response.body();
                    showToast(updateResponse.getMessage());
                    goToLoginPage();
                } else {
                    showToast("Gagal memperbarui password. Silakan coba lagi.");
                }
            }

            @Override
            public void onFailure(Call<UpdatePasswordResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showToast("Koneksi gagal. Silakan cek koneksi internet Anda.");
            }
        });
    }

    private void goToLoginPage() {
        Intent intent = new Intent(BuatpassActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(BuatpassActivity.this, message, Toast.LENGTH_SHORT).show();
    }
    public void lihatButtonClicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
