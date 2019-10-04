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
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Sessao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AddressDAO {
    private Context context;
    private String host = "https://pickupbsiapi.herokuapp.com";

    public AddressDAO(Context contexto){
        this.context = contexto;
    }

    public void criarEndereco(final Space space) throws JSONException {
        space.getAddress().setId(0);
        String url = host + "/spot";
        JSONObject postparams = new JSONObject();
        postparams.put("street", space.getAddress().getStreet());
        postparams.put("number", space.getAddress().getNumber());
        postparams.put("neighborhood", space.getAddress().getNeighboorhood());
        final AtomicInteger requestsCounter = new AtomicInteger(0);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    space.getAddress().setId(Integer.parseInt(response.get("person_id").toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Log.d("resposta", "onErrorResponse: " + error);
                    criarEndereco(space);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    if (space.getAddress().getId() != 0) {
                        try {
                            setEnderecoEspaco(space);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void setEnderecoEspaco(final Space space) throws JSONException {
        String url = host + "/spot/"+ String.valueOf(space.getId())+ "/address";
        JSONObject postparams = new JSONObject();
        postparams.put("person_id", space.getAddress().getId());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resposta setEndereco", String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    setEnderecoEspaco(space);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    }

    private void getEnderecoEspaco(final Space space) throws JSONException {
        String url = host + "/spot/"+ String.valueOf(space.getId())+ "/address";
        JSONObject postparams = new JSONObject();
        postparams.put("person_id", space.getAddress().getId());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resposta getEndereco", String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    setEnderecoEspaco(space);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    }

}
