package com.pickupapp.persistencia.retorno;

import com.google.gson.annotations.SerializedName;
import com.pickupapp.dominio.Player;
import com.pickupapp.dominio.Space;

import java.util.ArrayList;

public class Players {

    private ArrayList<Player> players;

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
