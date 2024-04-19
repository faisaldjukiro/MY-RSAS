package com.example.ovo.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ovo.R;

public class ListActivity extends AppCompatActivity {
    //    private TextView tv_namaLengkap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        if (intent != null) {
//            String kd_peg = intent.getStringExtra("kd_peg");
//            String token = intent.getStringExtra("token");
            String nama = intent.getStringExtra("nama");

            // Periksa jika data yang diterima tidak null
            if (nama != null) {
                // Lakukan sesuatu dengan data yang diterima
                // Contoh: Tampilkan data dalam TextView
//                TextView textViewKdPeg = findViewById(R.id.textViewKdPeg);
//                TextView textViewToken = findViewById(R.id.textViewToken);
                TextView textViewNama = findViewById(R.id.nama_dokter);

//                textViewKdPeg.setText(kd_peg);
//                textViewToken.setText(token);
                textViewNama.setText(nama);
            } else {
                // Tampilkan Toast jika data yang diterima null
                Toast.makeText(this, "Data tidak tersedia", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Tampilkan Toast jika intent null
            Toast.makeText(this, "Data tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }
}