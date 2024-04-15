package com.example.ovo.helper;

import com.example.ovo.R;
import com.example.ovo.model.MenuItem;

import java.util.ArrayList;

public class MenuDataUtil {
    public static ArrayList<MenuItem> getData() {
        ArrayList<MenuItem> menuItems = new ArrayList<>();

        menuItems.add(new MenuItem("tidak", R.drawable.baseline_blur_circular_24));
        menuItems.add(new MenuItem("tidak", R.drawable.baseline_blur_circular_24));
        menuItems.add(new MenuItem("tidak", R.drawable.baseline_blur_circular_24));
        menuItems.add(new MenuItem("tidak", R.drawable.baseline_blur_circular_24));
        menuItems.add(new MenuItem("tidak", R.drawable.baseline_blur_circular_24));
        menuItems.add(new MenuItem("tidak", R.drawable.baseline_blur_circular_24));
        menuItems.add(new MenuItem("tidak", R.drawable.baseline_blur_circular_24));
        menuItems.add(new MenuItem("tidak", R.drawable.baseline_blur_circular_24));

        return menuItems;
    }
}

