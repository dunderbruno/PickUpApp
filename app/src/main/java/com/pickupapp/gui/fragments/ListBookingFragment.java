package com.pickupapp.gui.fragments;


import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pickupapp.R;
import com.pickupapp.dominio.Booking;
import com.pickupapp.dominio.Person;
import com.pickupapp.dominio.adapter.BookingAdapter;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.BookingInterface;
import com.pickupapp.persistencia.SpaceInterface;
import com.pickupapp.persistencia.retorno.BookingsCall;
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
public class ListBookingFragment extends Fragment {
    private ArrayList<Booking> bookings;
    private ListView lista;
    private BookingAdapter adapter;
    private ProgressBar progressBar;

    public ListBookingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_list_players, container, false);
        buscarBookings(inflate);
        return inflate;
    }



    private void buscarBookings(View inflate) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookingInterface bookingInterface = retrofit.create(BookingInterface.class);
        String credentials = Sessao.getSessao(getContext()).getUsername()+":"+Sessao.getSessao(getContext()).getPassword();
        String token = Sessao.getSessao(getContext()).getToken();
        Call<BookingsCall> call = bookingInterface.getmyBooking(token);
        call.enqueue(new Callback<BookingsCall>() {
            @Override
            public void onResponse(Call<BookingsCall> call, Response<BookingsCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    return;
                }
                BookingsCall bookingsCall = response.body();
                bookings = bookingsCall.getBookings();
                lista = (ListView) inflate.findViewById(R.id.lista_players_fragment);
                adapter = new BookingAdapter(getActivity(), bookings);
                lista.setAdapter(adapter);
                //spacesList = spaces.getSpaces();   -- getPlayers()
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Fragment fragment = new ReservaFragment();
                        ReservaFragment.booking = bookings.get(position);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        transaction.replace(getId(), fragment);
                        transaction.commit();
                    }
                });
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
