package com.myspecialist.Adapter;

public class HomeAdapter {
    //Category Name as it is in Firebase. MUST be the same
    String Title;
    String Image;

    public HomeAdapter(String title, String image) {
        Title = title;
        Image = image;
    }

    public HomeAdapter() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
