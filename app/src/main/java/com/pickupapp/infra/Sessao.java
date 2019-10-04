package com.pickupapp.infra;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.pickupapp.dominio.User;

public class Sessao {
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public void editSessao(User user, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("usuario", json);
        editor.commit();
    }

    public void clear(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

    public static User getSessao(Context context) {
        Gson gson = new Gson();
        String json = getSharedPreferences(context).getString("usuario", "");
        return gson.fromJson(json, User.class);
    }

}
