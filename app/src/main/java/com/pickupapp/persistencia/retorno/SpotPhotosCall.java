package com.pickupapp.persistencia.retorno;

import com.google.gson.annotations.SerializedName;
import com.pickupapp.dominio.Photo;

import java.util.ArrayList;

public class SpotPhotosCall {
    @SerializedName("spot_photos")
    private ArrayList<Photo> photos;

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }
}
