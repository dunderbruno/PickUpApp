package com.pickupapp.dominio;

import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("filename")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
