package com.example.ovo.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ovo.ProfileFragment;
import com.example.ovo.R;
import com.example.ovo.view.fragment.BerandaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String namaLengkap = getIntent().getStringExtra("nama");
        BerandaFragment berandaFragment = BerandaFragment.newInstance("", "");

        loadFragment(new BerandaFragment());

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav_view);
        navigationView.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            fragment = new BerandaFragment();
        } else if (itemId == R.id.cari) {
//            fragment = new BerandaFragment();
        } else if (itemId == R.id.placeholder) {
//            fragment = new BerandaFragment();
        } else if (itemId == R.id.profile) {
            fragment = new ProfileFragment();
        } else if (itemId == R.id.setings) {
//            fragment = new BerandaFragment();
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
