package com.pickupapp.persistencia.retorno;

import com.google.gson.annotations.SerializedName;
import com.pickupapp.dominio.State;

import java.util.ArrayList;

public class StatesCall {
    @SerializedName("states")
    private ArrayList<State> estados;

    public ArrayList<State> getEstados() {
        return estados;
    }
}
