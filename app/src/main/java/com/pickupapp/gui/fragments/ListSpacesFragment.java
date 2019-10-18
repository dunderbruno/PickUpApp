package com.pickupapp.gui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pickupapp.R;
import com.pickupapp.dominio.Space;
import com.pickupapp.dominio.adapter.SpaceAdapter;
import com.pickupapp.gui.SpaceActivity;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.SpaceInterface;
import com.pickupapp.persistencia.retorno.Spots;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListSpacesFragment extends Fragment {
    private List<Space> spacesList;
    private ListView lista;
    private ArrayAdapter<Space> adapter;

    public ListSpacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.fragment_list_spaces, container, false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceInterface spaceInterface = retrofit.create(SpaceInterface.class);
        String credentials = Sessao.getSessao(getContext()).getUsername()+":"+Sessao.getSessao(getContext()).getPassword();
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<Spots> call = spaceInterface.getSpaces(auth, Sessao.getSessao(getContext()).getToken());
        call.enqueue(new Callback<Spots>() {
            @Override
            public void onResponse(Call<Spots> call, Response<Spots> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    return;
                }
                Log.d("resposta", "onResponse: "+response.body());
                Spots spaces = response.body();
                lista = (ListView) inflate.findViewById(R.id.lista_spaces_fragment);
                adapter = null;
                adapter = new SpaceAdapter(getActivity(), spaces.getSpaces());
                lista.setAdapter(adapter);
                spacesList = spaces.getSpaces();
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), SpaceActivity.class);
                        intent.putExtra("Nome", String.valueOf(spacesList.get(position).getName()));
                        intent.putExtra("Phone", String.valueOf(spacesList.get(position).getPhone()));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<Spots> call, Throwable t) {
                Log.d("resposta", "onResponse: "+ t);
            }
        });
        return inflate;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

    }

    private void clickListView(int position){

    }

}
