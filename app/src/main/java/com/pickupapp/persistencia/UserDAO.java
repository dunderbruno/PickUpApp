package com.pickupapp.persistencia;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pickupapp.dominio.User;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import android.util.Base64;
//import com.pickupapp.infra.Session;

public class UserDAO {
    private Context context;
    private String host = "https://pickupbsiapi.herokuapp.com";

    public UserDAO(Context contexto){
        this.context = contexto;
    }

    public String register(final User user) throws JSONException{
        String url = host + "/user";
        JSONObject postparams = new JSONObject();
//        postparams.put("x-access-token", Session.instance.getToken());
        final String[] idusuario = {""};
        postparams.put("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.PODXncdn8smjXC-GZfhaMXIJ9M4fYAvwfZUT9xNgO3Y");
        String auth = user.getUsername()+":"+user.getPassword();
        byte[] data = auth.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        postparams.put("Authorization", "Basic "+ base64);
        postparams.put("username", user.getUsername());
        postparams.put("password", user.getPassword());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    idusuario[0] = String.valueOf(response.get("new_user_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("resposta", String.valueOf(error));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = user.getUsername()+":"+user.getPassword();
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.PODXncdn8smjXC-GZfhaMXIJ9M4fYAvwfZUT9xNgO3Y");
                return params;
            }};
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(jsonObjectRequest);
        if (!idusuario[0].equals("")){
            Log.d("usuario", String.valueOf(idusuario[0]));
        }
        return ""; // TODO: retornar o id gerado
    }

}
