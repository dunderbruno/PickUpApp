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

import com.pickupapp.dominio.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.util.Base64;
//import com.pickupapp.infra.Session;

public class UserDAO {
    private Context context;
    private String host = "https://pickupbsiapi.herokuapp.com";

    public UserDAO(Context contexto){
        this.context = contexto;
    }

    public User register(final User user) throws JSONException{
        user.setId(0);
        String url = host + "/user";
        JSONObject postparams = new JSONObject();
        final String[] idusuario = {""};
        postparams.put("username", user.getUsername());
        postparams.put("password", user.getPassword());
        final AtomicInteger requestsCounter = new AtomicInteger(0);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("aqui", String.valueOf(response.get("new_user_id").toString()));
                    user.setId(Integer.parseInt(response.get("new_user_id").toString()));
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
        requestsCounter.incrementAndGet();
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestsCounter.decrementAndGet();
                if (requestsCounter.get() == 0) {
                    Log.d("aqui", String.valueOf(user.getId()));
                    if (user.getId() != 0) {
                        Log.d("aqui", "chegou2");
                        PersonDAO pessoa = new PersonDAO(context);
                        try {
                            pessoa.criarPessoa(user);
                            setGrupoUsuario(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return user;
    }

    private void setGrupoUsuario(final User user) throws JSONException {
        String url = host + "/user/"+ String.valueOf(user.getId())+ "/group";
        JSONObject postparams = new JSONObject();
        postparams.put("group", user.getGroup().getGroup_name());
        final AtomicInteger requestsCounter = new AtomicInteger(0);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resposta setgroup", String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    setGrupoUsuario(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        requestsCounter.incrementAndGet();
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestsCounter.decrementAndGet();
                if (requestsCounter.get() == 0) {
                    return;
                }
            }
        });
    }

    public User login(final User user) throws JSONException{
        user.setId(0);
        String url = host + "/user";
        JSONObject postparams = new JSONObject();
        final String[] idusuario = {""};
        postparams.put("username", user.getUsername());
        postparams.put("password", user.getPassword());
        final AtomicInteger requestsCounter = new AtomicInteger(0);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("aqui", String.valueOf(response.get("token").toString()));
                    user.setId(Integer.parseInt(response.get("token").toString()));
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
        return user;
    }

}
