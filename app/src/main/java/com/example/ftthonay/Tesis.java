package com.example.ftthonay;

import android.net.Uri;

public class Tesis {

    private Uri imageURL;
    private String imageName;
    private String tarih;

    public Tesis(Uri imageURL, String tarih,  String imageName) {
        this.imageURL = imageURL;
        this.imageName = imageName;
        this.tarih = tarih;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Tesis() {
    }

    public Uri getImageURL() {
        return imageURL;
    }

    public void setImageURL(Uri imageURL) {
        this.imageURL = imageURL;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
