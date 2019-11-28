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
        buscarBookings();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                buscarPlayers(bookings.get(i).getId());
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
        call = playerInterface.getBookingPlayers(Sessao.getSessao(getContext()).getToken(), booking_id);
        call.enqueue(new Callback<Players>() {
            @Override
            public void onResponse(Call<Players> call, Response<Players> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("resposta", "onResponse: "+response.body());
                Players spaces = response.body();
                adapter = new PlayerAdapter(getActivity(), spaces.getPlayers());
                lista.setAdapter(adapter);
                playersList = spaces.getPlayers();
            }

            @Override
            public void onFailure(Call<Players> call, Throwable t) {
                Log.d("resposta", "onResponse: "+ t);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void enviarConvite() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InviteInterface inviteInterface = retrofit.create(InviteInterface.class);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<SetCall> call = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("player_id", jogador);
        params.put("booking_id", String.valueOf(spinner.getSelectedItemId()));
        call = inviteInterface.registerInvite(Sessao.getSessao(getContext()).getToken(), params);
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
                convite.setVisibility(View.INVISIBLE);
                lista.setVisibility(View.VISIBLE);
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

    private void buscarBookings() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookingInterface bookingInterface = retrofit.create(BookingInterface.class);
        User user = Sessao.getSessao(getContext());
        Call<BookingsCall> call;
        if (user.getGroup().getGroup_name().equals("2")){
            call = bookingInterface.getmyBookingSpots(user.getToken());
        }else{
            call = bookingInterface.getmyBooking(user.getToken());
        }
        call.enqueue(new Callback<BookingsCall>() {
            @Override
            public void onResponse(Call<BookingsCall> call, Response<BookingsCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                Log.d("resposta", "onResponse: "+response);
                BookingsCall bookingsCall = response.body();
                bookings = bookingsCall.getBookings();
                ArrayList<String> listString = new ArrayList<>();
                for (Booking n : bookings) {
                    listString.add(n.getSpot_name()+", "+n.getDay()+", \n \n"+n.getStart_time()+"-"+n.getEnd_time());
                }
                ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listString);
                cidadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(cidadeAdapter);
                progressBar.setVisibility(View.INVISIBLE);
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
