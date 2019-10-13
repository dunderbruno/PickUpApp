package com.pickupapp.gui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.pickupapp.R;
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.SpaceAdapter;
import com.pickupapp.dominio.User;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.SpaceDAO;
import com.pickupapp.persistencia.SpaceInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListSpacesFragment extends Fragment {
    private ArrayList<Space> spaces;

    public ListSpacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_list_spaces, container, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceInterface spaceInterface = retrofit.create(SpaceInterface.class);
        String credentials = Sessao.getSessao(getContext()).getUsername()+":"+Sessao.getSessao(getContext()).getPassword();
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        Log.d("resposta", auth);
        String token = Sessao.getSessao(getContext()).getToken();
        Log.d("resposta1", token);
        Call<List<Space>> call = spaceInterface.getSpaces(auth, Sessao.getSessao(getContext()).getToken());
        call.enqueue(new Callback<List<Space>>() {
            @Override
            public void onResponse(Call<List<Space>> call, Response<List<Space>> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    return;
                }
                Log.d("resposta", "onResponse: "+response.body());
            }

            @Override
            public void onFailure(Call<List<Space>> call, Throwable t) {
                Log.d("resposta", "onResponse: "+ t);
            }
        });
        return inflate;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
//        ListView lista = (ListView) view.findViewById(R.id.lista_spaces_fragment);
//        ArrayAdapter<Space> adapter = null;
//        adapter = new SpaceAdapter(getActivity(), spaces);
//        lista.setAdapter(adapter);
    }

}
