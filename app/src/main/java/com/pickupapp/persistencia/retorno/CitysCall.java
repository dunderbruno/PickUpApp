package com.pickupapp.persistencia.retorno;

import com.google.gson.annotations.SerializedName;
import com.pickupapp.dominio.City;

import java.util.ArrayList;

public class CitysCall {
    @SerializedName("cities")
    private ArrayList<City> cidades;

    public ArrayList<City> getCidades() {
        return cidades;
    }
}
