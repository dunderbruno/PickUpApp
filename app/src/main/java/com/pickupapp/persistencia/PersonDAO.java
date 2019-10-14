package com.pickupapp.persistencia;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pickupapp.dominio.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PersonDAO {
    private Context context;
    private String host = "https://pickupbsiapi.herokuapp.com";

    public PersonDAO(Context contexto){
        this.context = contexto;
    }



    private void getPessoaUsuario(final User user) throws JSONException {
        String url = host + "/user/"+ String.valueOf(user.getId())+ "/person";
        JSONObject postparams = new JSONObject();
        postparams.put("person_id", user.getPerson().getId());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("resposta getpessoa", String.valueOf(response.toString()));
                    user.getPerson().setName(String.valueOf(response.get("name").toString()));
                    user.getPerson().setSurname(String.valueOf(response.get("surname").toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
    }
}
