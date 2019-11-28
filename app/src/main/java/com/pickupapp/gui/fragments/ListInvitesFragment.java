package com.pickupapp.gui.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.pickupapp.R;
import com.pickupapp.dominio.Booking;
import com.pickupapp.dominio.Invite;
import com.pickupapp.dominio.Player;
import com.pickupapp.dominio.User;
import com.pickupapp.dominio.adapter.InviteAdapter;
import com.pickupapp.dominio.adapter.PlayerAdapter;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.BookingInterface;
import com.pickupapp.persistencia.InviteInterface;
import com.pickupapp.persistencia.PlayerInterface;
import com.pickupapp.persistencia.retorno.BookingsCall;
import com.pickupapp.persistencia.retorno.Invites;
import com.pickupapp.persistencia.retorno.Players;
import com.pickupapp.persistencia.retorno.SetCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListInvitesFragment extends Fragment {
    private ArrayList<Invite> invitesList;
    private ListView lista;
    private ArrayAdapter<Invite> adapter;
    private ProgressBar progressBar;
    private Spinner spinner;
    private LinearLayout convite;
    private String invite;
    private String booking;
    private String player;


    public ListInvitesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_list_invites, container, false);
        progressBar = inflate.findViewById(R.id.progressBarListPlayers);
        lista = inflate.findViewById(R.id.lista_players_fragment);
        buscarInvites();
        return inflate;
    }



    private void buscarInvites() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InviteInterface inviteInterface = retrofit.create(InviteInterface.class);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<Invites> call = null;
        call = inviteInterface.getInvites(token);
        call.enqueue(new Callback<Invites>() {
            @Override
            public void onResponse(Call<Invites> call, Response<Invites> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("resposta", "onResponse: "+response.body());
                Invites spaces = response.body();
                adapter = new InviteAdapter(getActivity(), spaces.getInvites());
                lista.setAdapter(adapter);
                invitesList = spaces.getInvites();
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Convidar")
                                .setMessage("Deseja Aceitar o convite?")
                                .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        invite = invitesList.get(position).getInvite_id();
                                        booking = invitesList.get(position).getBooking_id();
                                        player = invitesList.get(position).getPlayer_id();
                                        aceitarConvite();
                                    }
                                })
                                .setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Invites> call, Throwable t) {
                Log.d("resposta", "onResponse: "+ t);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void aceitarConvite() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InviteInterface inviteInterface = retrofit.create(InviteInterface.class);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<SetCall> call = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("player_id", player);
        params.put("booking_id", booking);
        params.put("invite_id", booking);
        call = inviteInterface.acceptInvite(Sessao.getSessao(getContext()).getToken(), params);
        call.enqueue(new Callback<SetCall>() {
            @Override
            public void onResponse(Call<SetCall> call, Response<SetCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(),"Ocorreu um erro no envio do convite!",Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<SetCall> call, Throwable t) {
                Log.d("resposta", "onResponse: "+ t);
                Toast.makeText(getContext(),"Ocorreu um erro no envio do convite!",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

    }

}
