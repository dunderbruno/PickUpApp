package com.pickupapp.persistencia;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

import com.pickupapp.dominio.User;
import com.pickupapp.infra.Session;

public class UserDAO {
    private Context context;
    private String host = "https://pickupbsiapi.herokuapp.com";


    public long register(User user) throws JSONException {
        String url = host + "/user";
        JSONObject postparams = new JSONObject();
        postparams.put("x-access-token", Session.instance.getToken());
        postparams.put("username", user.getUsername());
        postparams.put("password", user.getPassword());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO: o que fazer quando a requisição funcionar
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
            }
        });
        return 0; // TODO: retornar o id gerado
    }

}
