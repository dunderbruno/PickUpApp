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
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Sessao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class SpaceDAO {
    private Context context;
    private String host = "https://pickupbsiapi.herokuapp.com";

    public SpaceDAO(Context contexto){this.context = contexto;}

    public Space register(final Space space) throws JSONException {
        space.setId(0);
        String url = host + "/spot";
        JSONObject postparams = new JSONObject();
        final String[] idusuario = {""};
        postparams.put("spot_name", space.getAddress());
        final AtomicInteger requestsCounter = new AtomicInteger(0);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("aqui", String.valueOf(response.get("spot_id").toString()));
                    space.setId(Integer.parseInt(response.get("spot_id").toString()));
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
                User usuarioSessao = Sessao.getSessao(context);
                String credentials = usuarioSessao.getUsername()+":"+usuarioSessao.getPassword();
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
                    Log.d("aqui", String.valueOf(space.getId()));
                    if (space.getId() != 0) {
                        AddressDAO endereco = new AddressDAO(context);
                        try {
                            endereco.criarEndereco(space);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return space;
    }

    public Space getSpace() throws JSONException {
        final Space space = new Space();
        String url = host + "/spot";
        JSONObject postparams = new JSONObject();
        final String[] idusuario = {""};
        postparams.put("spot_name", space.getAddress());
        final AtomicInteger requestsCounter = new AtomicInteger(0);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("aqui", String.valueOf(response.get("spot_id").toString()));
                    space.setId(Integer.parseInt(response.get("spot_id").toString()));
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
                User usuarioSessao = Sessao.getSessao(context);
                String credentials = usuarioSessao.getUsername()+":"+usuarioSessao.getPassword();
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
                    Log.d("aqui", String.valueOf(space.getId()));
                    if (space.getId() != 0) {
                        AddressDAO endereco = new AddressDAO(context);
                        try {
                            endereco.criarEndereco(space);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return space;
    }

    public ArrayList<Space> getSpaces() throws JSONException {
        final ArrayList<Space> space = new ArrayList<>();
        String url = host + "/spot";
        final JSONObject[] resposta = new JSONObject[1];
        JSONObject postparams = new JSONObject();
        final AtomicInteger requestsCounter = new AtomicInteger(0);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, postparams, future,future){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                User usuarioSessao = Sessao.getSessao(context);
                String credentials = usuarioSessao.getUsername()+":"+usuarioSessao.getPassword();
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("x-access-token", Sessao.getSessao(context).getToken());
                return params;
            }};
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        requestQueue.add(jsonObjectRequest);
        try {
//            boolean status = future.isDone();
//            while (!status){
//                Log.d("resposta", "register: "+ future.isDone());
//                status = future.isDone();
//            }
            JSONObject response = future.get();
            if (response.get("spots") != "") {
                JSONArray jsonArray = null;
                try {
                    jsonArray= response.getJSONArray("spots");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try{
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        Space space1 = new Space();
                        space1.setName(explrObject.getString("name"));
                        space1.setId(Long.parseLong(explrObject.getString("id")));
                        space.add(space1);
                        Log.d("Resposta", "onRequestFinished: "+explrObject.toString());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            return space;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return space;
        }
    }
}
