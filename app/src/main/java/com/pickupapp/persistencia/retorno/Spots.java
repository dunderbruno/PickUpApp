package com.pickupapp.persistencia.retorno;

import com.google.gson.annotations.SerializedName;
import com.pickupapp.dominio.Space;

import java.util.ArrayList;
import java.util.List;

public class Spots {

    @SerializedName("spots")
    private ArrayList<Space> spaces;

    public ArrayList<Space> getSpaces() {
        return spaces;
    }
}
