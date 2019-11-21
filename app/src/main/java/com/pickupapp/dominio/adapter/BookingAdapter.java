package com.pickupapp.dominio.adapter;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pickupapp.R;
import com.pickupapp.dominio.Booking;
import com.pickupapp.dominio.Referee;
import com.pickupapp.dominio.Space;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.SpaceInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingAdapter extends ArrayAdapter<Booking> {

    private final Context context;
    private final ArrayList<Booking> bookings;


    public BookingAdapter(Context context, ArrayList<Booking> bookings){
        super(context, R.layout.linha_booking, bookings);
        this.context = context;
        this.bookings = bookings;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.linha_booking, parent, false);

        TextView nomeNick = rowView.findViewById(R.id.id_player_nic);
        TextView primeiroNome = rowView.findViewById(R.id.id_player_primeiro_nome);
        TextView email = rowView.findViewById(R.id.id_player_email);
                nomeNick.setText(bookings.get(position).getSpot_name());
                String primeiroNomeSet = bookings.get(position).getStart_time();
                String segundooNomeSet = bookings.get(position).getEnd_time();
                String texto = primeiroNomeSet + " " + segundooNomeSet;
                primeiroNome.setText(texto);
                email.setText(bookings.get(position).getDay());
        return rowView;
    }


}
