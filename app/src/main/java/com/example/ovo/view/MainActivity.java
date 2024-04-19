package com.example.ovo.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ovo.R;
import com.example.ovo.adapter.ChipAdapter;
import com.example.ovo.adapter.FragmentAdapter;
import com.example.ovo.adapter.MenuFinanceAdapter;
import com.example.ovo.adapter.SliderPagerAdapter;
import com.example.ovo.api.ApiService;
import com.example.ovo.api.JasaSatuResponse;
import com.example.ovo.api.RetrofitInstance;
import com.example.ovo.databinding.ActivityMainBinding;
import com.example.ovo.helper.BannerSlider;
import com.example.ovo.helper.SliderIndicator;
import com.example.ovo.model.MenuItem;
import com.example.ovo.view.fragment.FragmentSlider;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView chipView;
    private ViewPager2 viewPager;
    private FragmentAdapter fragmentAdapter;
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;
    private LinearLayout mLinearLayout;
    private BannerSlider bannerSlider;
    private TextView tvnamaLengkap , tvjasaJumlah, tvkd_peg;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bannerSlider = view.findViewById(R.id.sliderView);
        mLinearLayout = view.findViewById(R.id.pagesContainer);
        tvnamaLengkap = view.findViewById(R.id.nama_lengkap);
        tvjasaJumlah = view.findViewById(R.id.jasajumlah);
        tvkd_peg = view.findViewById(R.id.kd_peg);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.token_key), "token");
        String namaLengkap = getIntent().getStringExtra("nama");
        String kdPeg = getIntent().getStringExtra("kd_peg");
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);;
        Call<JasaSatuResponse> call = apiService.getData(token, kdPeg);
        call.enqueue(new Callback<JasaSatuResponse>() {
            @Override
            public void onResponse(Call<JasaSatuResponse> call, Response<JasaSatuResponse> response) {
                if (response.isSuccessful()) {
                    JasaSatuResponse jasaSatuResponse = response.body();
                    if (jasaSatuResponse != null) {
                        JasaSatuResponse.DataModel dataModel = jasaSatuResponse.getData();
                        String jumlah = dataModel.getJumlah();

                        double jumlahDouble = Double.parseDouble(jumlah);
                        DecimalFormat format = new DecimalFormat("#,###");
                        String jumlahRupiah = "Rp " + format.format(jumlahDouble);

                        tvjasaJumlah.setText(jumlahRupiah);
                    } else {
                        Toast.makeText(MainActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JasaSatuResponse> call, Throwable t) {
                // Tangani kesalahan jaringan
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (namaLengkap != null && kdPeg != null) {
            tvnamaLengkap.setText(namaLengkap);
        } else {
            Toast.makeText(MainActivity.this, "Gagal Mendapatkan Data", Toast.LENGTH_SHORT).show();
        }
        setupSlider();

        RecyclerView recyclerView = binding.recyclerviewFinance;
        chipView = binding.mainMenu;
        viewPager = binding.viewPager;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<MenuItem> data = new ArrayList<>();
        data.add(new MenuItem("", R.drawable.baseline_blur_circular_24));
        data.add(new MenuItem("", R.drawable.baseline_blur_circular_24));
        data.add(new MenuItem("", R.drawable.baseline_blur_circular_24));
        data.add(new MenuItem("", R.drawable.baseline_blur_circular_24));

        MenuFinanceAdapter adapter = new MenuFinanceAdapter(this, data);
        recyclerView.setAdapter(adapter);

        recyclerView.post(() -> adapter.justifyItems(recyclerView));

        String[] itemList = {"Favorit", "Pilihan Lain", "Pilihan Lain", "Pilihan Lain"};

        ChipAdapter chipAdapter = new ChipAdapter(itemList, viewPager);
        chipView.setAdapter(chipAdapter);
        LinearLayoutManager chipManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        chipView.setLayoutManager(chipManager);

        fragmentAdapter = new FragmentAdapter(this, itemList.length);
        viewPager.setAdapter(fragmentAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                chipAdapter.setSelectedItem(position);
                chipView.smoothScrollToPosition(position);
            }
        });
    }



    private void setupSlider() {
        bannerSlider.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(FragmentSlider.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/19/85531617/85531617_17b56894-2608-4509-a4f4-ad86aa5d3b62.jpg"));
        fragments.add(FragmentSlider.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/19/85531617/85531617_7da28e4c-a14f-4c10-8fec-845578f7d748.jpg"));
        fragments.add(FragmentSlider.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/18/85531617/85531617_87a351f9-b040-4d90-99f4-3f3e846cd7ef.jpg"));
        fragments.add(FragmentSlider.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/20/85531617/85531617_03e76141-3faf-45b3-8bcd-fc0962836db3.jpg"));

        mAdapter = new SliderPagerAdapter(getSupportFragmentManager(), fragments);
        bannerSlider.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(this, mLinearLayout, bannerSlider, R.drawable.indicator);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }

    public void lihatButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
