package com.example.ovo.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ovo.R;
import com.example.ovo.RincianDokterActivity;
import com.example.ovo.adapter.JasaAdapter;
import com.example.ovo.api.ApiService;
import com.example.ovo.api.JasaListResponse;
import com.example.ovo.api.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    private TextView tv_namaLengkap;

    private RecyclerView recyclerView;
    private JasaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        tv_namaLengkap = findViewById(R.id.nama_pegawai);
        ImageButton backButton = findViewById(R.id.back_btn);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ApiService apiService = RetrofitInstance.getRetrofitInstance().create(ApiService.class);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "");
        String kdPeg = sharedPref.getString(getString(R.string.kd_peg_key), "");
        String nama = sharedPref.getString(getString(R.string.nama_lengkap_key), "");

        Call<JasaListResponse> call = apiService.getList(token, kdPeg);
        call.enqueue(new Callback<JasaListResponse>() {
            @Override
            public void onResponse(Call<JasaListResponse> call, Response<JasaListResponse> response) {
                if (response.isSuccessful()) {
                    List<JasaListResponse.Jasa> jasaList = response.body().getData();
                    adapter = new JasaAdapter(ListActivity.this, jasaList);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new JasaAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            // Mendapatkan objek Jasa yang diklik
                            JasaListResponse.Jasa clickedJasa = jasaList.get(position);

                            String blnTahun = clickedJasa.getBlnTahun();

                            Intent intent = new Intent(ListActivity.this, RincianDokterActivity.class);
                            intent.putExtra("blntahun", blnTahun);
//                            Toast.makeText(ListActivity.this, blnTahun, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    });

                } else {
                    Toast.makeText(ListActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JasaListResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ListActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });



        Intent intent = getIntent();
        if (intent != null) {

            if (tv_namaLengkap != null) {
                if (nama != null) {
                    tv_namaLengkap.setText(nama);
                } else {
                    Toast.makeText(ListActivity.this, "Gagal Mendapatkan Data", Toast.LENGTH_SHORT).show();
                }
                tv_namaLengkap.setText(nama);
            } else {
                Toast.makeText(this, "Data tidak tersedia", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Data tidak tersedia", Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}