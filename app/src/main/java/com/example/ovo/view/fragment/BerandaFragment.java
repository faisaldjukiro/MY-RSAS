package com.example.ovo.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ovo.R;
import com.example.ovo.adapter.ChipAdapter;
import com.example.ovo.adapter.FragmentAdapter;
import com.example.ovo.adapter.MenuFinanceAdapter;
import com.example.ovo.adapter.SliderPagerAdapter;
import com.example.ovo.api.ApiService;
import com.example.ovo.api.JasaSatuResponse;
import com.example.ovo.api.RetrofitInstance;
import com.example.ovo.helper.BannerSlider;
import com.example.ovo.helper.SliderIndicator;
import com.example.ovo.model.MenuItem;
import com.example.ovo.view.ListActivity;
import com.example.ovo.view.MainActivity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BerandaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;
    private String mParam1;
    private String mParam2;



    private BannerSlider bannerSlider;
    private LinearLayout mLinearLayout;
    private TextView tvnamaLengkap, tvjasaJumlah, tvkd_peg;

    private RecyclerView chipView;
    private ViewPager2 viewPager;
    private FragmentAdapter fragmentAdapter;

    public BerandaFragment() {
    }

    public static BerandaFragment newInstance(String param1, String param2 ) {
        BerandaFragment fragment = new BerandaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        bannerSlider = view.findViewById(R.id.sliderView);
        mLinearLayout = view.findViewById(R.id.pagesContainer);
        tvjasaJumlah = view.findViewById(R.id.jasajumlah);
        tvkd_peg = view.findViewById(R.id.kd_peg);
        chipView = view.findViewById(R.id.mainMenu);
        tvnamaLengkap = view.findViewById(R.id.nama_lengkap);

        SharedPreferences sharedPref = requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String namaLengkap= sharedPref.getString(getString(R.string.nama_lengkap_key), "kd_peg");
        tvnamaLengkap.setText(namaLengkap);
        Button lihatButton = view.findViewById(R.id.lihat);

        lihatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lihatButtonClicked();
            }
        });

        return view;
    }

    private void lihatButtonClicked() {
        Intent intent = new Intent(requireContext(), ListActivity.class);

        startActivity(intent);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromBackend(getTokenFromSharedPreferences(), getKdPegFromSharedPreferences());

        setupSlider();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewFinance);
        viewPager = view.findViewById(R.id.viewPager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<MenuItem> data = new ArrayList<>();
        data.add(new MenuItem("", R.drawable.baseline_blur_circular_24));
        data.add(new MenuItem("", R.drawable.baseline_blur_circular_24));
        data.add(new MenuItem("", R.drawable.baseline_blur_circular_24));
        data.add(new MenuItem("", R.drawable.baseline_blur_circular_24));

        MenuFinanceAdapter adapter = new MenuFinanceAdapter(requireContext(), data);
        recyclerView.setAdapter(adapter);

        recyclerView.post(() -> adapter.justifyItems(recyclerView));

        String[] itemList = {"Favorit", "Pilihan Lain", "Pilihan Lain", "Pilihan Lain"};

        ChipAdapter chipAdapter = new ChipAdapter(itemList, viewPager);
        chipView.setAdapter(chipAdapter);
        LinearLayoutManager chipManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        chipView.setLayoutManager(chipManager);

        MainActivity mainActivity = (MainActivity) requireActivity();
        fragmentAdapter = new FragmentAdapter(mainActivity, itemList.length);
        viewPager.setAdapter(fragmentAdapter);


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                chipAdapter.setSelectedItem(position);
                chipView.smoothScrollToPosition(position);
            }
        });
    }

    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.token_key), "token");
    }

    private String getKdPegFromSharedPreferences() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.kd_peg_key), "kd_peg");
    }




    private void getDataFromBackend(String token, String kdPeg) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);
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
                        Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JasaSatuResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSlider() {
        bannerSlider.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("https://asset-2.tstatic.net/gorontalo/foto/bank/images/Rumah-Sakit-Umum-Daerah-RSUD-Prof-Dr-dr-Aloei-Saboe.jpg"));
        fragments.add(FragmentSlider.newInstance("https://asset-2.tstatic.net/gorontalo/foto/bank/images/Rumah-Sakit-Umum-Daerah-Prof-Dr-dr-Aloei-Saboe.jpg"));
        fragments.add(FragmentSlider.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/19/85531617/85531617_17b56894-2608-4509-a4f4-ad86aa5d3b62.jpg"));
        fragments.add(FragmentSlider.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/19/85531617/85531617_7da28e4c-a14f-4c10-8fec-845578f7d748.jpg"));
        fragments.add(FragmentSlider.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/18/85531617/85531617_87a351f9-b040-4d90-99f4-3f3e846cd7ef.jpg"));
        fragments.add(FragmentSlider.newInstance("https://ecs7.tokopedia.net/img/banner/2020/4/20/85531617/85531617_03e76141-3faf-45b3-8bcd-fc0962836db3.jpg"));
        mAdapter = new SliderPagerAdapter(getChildFragmentManager(), fragments);
        bannerSlider.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(requireContext(), mLinearLayout, bannerSlider, R.drawable.indicator);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }
}
