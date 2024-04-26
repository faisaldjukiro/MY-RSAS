package com.example.ovo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ovo.api.ApiService;
import com.example.ovo.api.DokterResponse;
import com.example.ovo.api.RetrofitInstance;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RincianDokterActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_dokter);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "");
        String kdPeg = sharedPref.getString(getString(R.string.kd_peg_key), "");
        String nama = sharedPref.getString(getString(R.string.nama_lengkap_key), "");
        String blnthn = getIntent().getStringExtra("blntahun");
//        Toast.makeText(RincianDokterActivity.this, blnthn, Toast.LENGTH_SHORT).show();


        getDataFromApi(token, kdPeg, blnthn);
    }

    private void getDataFromApi(String token, String kdPeg, String blnthn) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<DokterResponse> call = apiService.getData(token, kdPeg, blnthn);
        call.enqueue(new Callback<DokterResponse>() {
            @Override
            public void onResponse(Call<DokterResponse> call, Response<DokterResponse> response) {
                if (response.isSuccessful()) {
                    DokterResponse dokterResponse = response.body();
                    if (dokterResponse != null && dokterResponse.getStatus().equals("success")) {
                        displayDataInTable(dokterResponse);

                    } else {
                        Toast.makeText(RincianDokterActivity.this, "Data Tidak Di Temukan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RincianDokterActivity.this, "Data Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DokterResponse> call, Throwable t) {
            }
        });
    }

    private void displayDataInTable(DokterResponse dokterResponse) {
        // Mendapatkan array data dari respons
        DokterResponse.Data[] dataArray = dokterResponse.getData();

        // Mendapatkan TextView dari XML untuk setiap data yang ingin ditampilkan
        TextView textViewKasus = findViewById(R.id.kasus);
        TextView textViewPasien = findViewById(R.id.pasien);
        TextView textViewTindakan = findViewById(R.id.tindakan);
        TextView textViewJasa = findViewById(R.id.jasa);
        TextView textViewKlem = findViewById(R.id.klem);

        // Reset TextView untuk membersihkan tampilan sebelum menampilkan data baru
        textViewKasus.setText("");
        textViewPasien.setText("");
        textViewTindakan.setText("");
        textViewJasa.setText("");
        textViewKlem.setText("");


        String previousKasus = "";
        String previousPasien = "";

        // Membuat objek untuk mengonversi nilai ke format mata uang Rupiah
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatRupiah.setMaximumFractionDigits(0);

        for (DokterResponse.Data data : dataArray) {
            String kasus = data.getKasus();


            for (DokterResponse.DataKasus dataKasus : data.getData_kasus()) {
                String pasien = dataKasus.getPasien();

                for (DokterResponse.DataKasus.DataPasien dataPasien : dataKasus.getData_pasien()) {
                    String tindakan = dataPasien.getTindakan();
                    String jumlah = dataPasien.getJumlah();
                    String klem = dataPasien.getKlem();

                    if (kasus.equals(previousKasus)) {
                        kasus = "";
                    } else {
                        previousKasus = kasus;
                    }

                    if (pasien.equals(previousPasien)) {
                        pasien = "";
                    } else {
                        previousPasien = pasien;
                    }

                    // Konversi nilai jasa dan klem ke format mata uang Rupiah
                    String formattedJasa = formatRupiah.format(Double.parseDouble(jumlah));
                    String formattedKlem = formatRupiah.format(Double.parseDouble(klem));

                    // Menambahkan spasi setelah simbol "Rp"
                    formattedJasa = formattedJasa.replace("Rp", "Rp ");
                    formattedKlem = formattedKlem.replace("Rp", "Rp ");

                    textViewKasus.append(kasus + "\n");
                    textViewPasien.append(pasien + "\n");
                    textViewTindakan.append(tindakan + "\n");
                    textViewJasa.append(formattedJasa + "\n");
                    textViewKlem.append(formattedKlem + "\n");
                }
            }
        }
    }
}
