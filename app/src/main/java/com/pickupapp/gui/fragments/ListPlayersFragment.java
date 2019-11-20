package com.pickupapp.gui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.pickupapp.R;
import com.pickupapp.dominio.Person;
import com.pickupapp.dominio.adapter.PlayerAdapter;
import com.pickupapp.dominio.adapter.SpaceAdapter;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.SpaceInterface;
import com.pickupapp.persistencia.retorno.Spots;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPlayersFragment extends Fragment {
    private List<Person> playersList;
    private ListView lista;
    private ArrayAdapter<Person> adapter;
    private ProgressBar progressBar;

    public ListPlayersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_list_players, container, false);
        progressBar = inflate.findViewById(R.id.progressBarListPlayers);
        return inflate;
    }



    private void buscarPlayers(View inflate) {

        //ALTERAR VARIÁVEIS
        //DE RETORNO DO BANCO


        progressBar.setVisibility(View.VISIBLE);
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
        Call<Spots> call = null;
        if (Sessao.getSessao(getContext()).getGroup().getGroup_name().equals("2")){
            call = spaceInterface.getMySpaces(auth, Sessao.getSessao(getContext()).getToken());
        }else{
            call = spaceInterface.getSpaces(auth, Sessao.getSessao(getContext()).getToken());
        }
        call.enqueue(new Callback<Spots>() {
            @Override
            public void onResponse(Call<Spots> call, Response<Spots> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("resposta", "onResponse: "+response.body());
                Spots spaces = response.body();
                lista = (ListView) inflate.findViewById(R.id.lista_players_fragment);
                adapter = null;
                //


                //adapter = new PlayerAdapter(getActivity(), spaces.getSpaces());  -- getPlayers()
                lista.setAdapter(adapter);
                //spacesList = spaces.getSpaces();   -- getPlayers()
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Fragment fragment = new SpaceFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(getId(), fragment);
                        transaction.commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<Spots> call, Throwable t) {
                Log.d("resposta", "onResponse: "+ t);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

    }

}
