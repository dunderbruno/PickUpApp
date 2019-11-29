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
import com.pickupapp.dominio.Player;
import com.pickupapp.dominio.User;
import com.pickupapp.dominio.adapter.PlayerAdapter;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.BookingInterface;
import com.pickupapp.persistencia.InviteInterface;
import com.pickupapp.persistencia.PlayerInterface;
import com.pickupapp.persistencia.retorno.BookingsCall;
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
public class ListPlayersTeamFragment extends Fragment {
    private ArrayList<Player> playersList;
    private ListView lista;
    private ArrayAdapter<Player> adapter;
    private ProgressBar progressBar;
    private ArrayList<Booking> bookings;
    private Spinner spinner;
    private String jogador;
    private LinearLayout convite;
    private Button convidar;


    public ListPlayersTeamFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_list_players_team, container, false);
        progressBar = inflate.findViewById(R.id.progressBarListPlayers);
        spinner = inflate.findViewById(R.id.spinnerBooking);
        lista = inflate.findViewById(R.id.lista_players_fragment_team);
        lista.setVisibility(View.VISIBLE);
        buscarBookings();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    buscarPlayers(bookings.get(i-1).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return inflate;
    }



    private void buscarPlayers(String booking_id) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PlayerInterface playerInterface = retrofit.create(PlayerInterface.class);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<Players> call = null;
        call = playerInterface.getBookingPlayers(token, booking_id);
        call.enqueue(new Callback<Players>() {
            @Override
            public void onResponse(Call<Players> call, Response<Players> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                progressBar.setVisibility(View.GONE);
                Log.d("resposta", "onResponse: "+response.body().getPlayers().get(0).getName());
                Players players = response.body();
                adapter = new PlayerAdapter(getActivity(), players.getPlayers());
                lista.setAdapter(adapter);
                playersList = players.getPlayers();
            }

            @Override
            public void onFailure(Call<Players> call, Throwable t) {
                Log.d("resposta", "onResponse: "+ t);
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void buscarBookings() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookingInterface bookingInterface = retrofit.create(BookingInterface.class);
        User user = Sessao.getSessao(getContext());
        Call<BookingsCall> call;
        call = bookingInterface.getmyBookingInvited(user.getToken());
        call.enqueue(new Callback<BookingsCall>() {
            @Override
            public void onResponse(Call<BookingsCall> call, Response<BookingsCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                Log.d("resposta", "onResponse: "+response);
                BookingsCall bookingsCall = response.body();
                bookings = bookingsCall.getBookings();
                ArrayList<String> listString = new ArrayList<>();
                listString.add("Selecione uma reserva");
                for (Booking n : bookings) {
                    listString.add(n.getSpot_name()+", "+n.getDay()+", \n \n"+n.getStart_time()+"-"+n.getEnd_time());
                }
                ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listString);
                cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(cidadeAdapter);
                spinner.setSelection(0);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<BookingsCall> call, Throwable t) {
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

    }

}
