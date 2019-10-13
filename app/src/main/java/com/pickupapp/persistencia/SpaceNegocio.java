package com.pickupapp.persistencia;

import android.content.Context;
import android.util.Log;

import com.pickupapp.dominio.Space;

import org.json.JSONException;

import java.util.ArrayList;

public class SpaceNegocio {
    private Context context;
    private String host = "https://pickupbsiapi.herokuapp.com";

    public SpaceNegocio(Context contexto){this.context = contexto;}

    public ArrayList<Space> buscarEspacos(){
        final SpaceDAO spaceDAO = new SpaceDAO(context);
        final ArrayList<Space>[] response = new ArrayList[]{new ArrayList<>()};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response[0] = spaceDAO.getSpaces();
                    Log.d("resposta", "retornarSpaces: "+ response[0].toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("resposta", "retornarSpaces: "+ response[0].toString());
            }
        });
        thread.start();
        thread.getPriority();
        Log.d("resposta", "buscarEspacos: "+thread.getState());
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response[0];
    }
}
