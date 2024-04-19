package com.example.ovo.model;

public class MyMenuItem {
    private final String title;
    private final int imageResource;
    private final int itemId; // Mengubah tipe data dari String menjadi int

    public MyMenuItem(String title, int imageResource, int itemId) { // Mengubah tipe data parameter constructor
        this.title = title;
        this.imageResource = imageResource;
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResource() {
        return imageResource;
    }

//    public int getItemId() {
//        return itemId;
//    }
}
