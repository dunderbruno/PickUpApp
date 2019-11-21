package com.pickupapp.dominio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pickupapp.R;
import com.pickupapp.dominio.Booking;

import java.util.ArrayList;

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

        TextView nomeNick = rowView.findViewById(R.id.nome_spot_booking);
        TextView primeiroNome = rowView.findViewById(R.id.horario_spot_booking);
        TextView email = rowView.findViewById(R.id.data_spot_booking);
        nomeNick.setText(bookings.get(position).getSpot_name());
        String primeiroNomeSet = bookings.get(position).getStart_time();
        String segundooNomeSet = bookings.get(position).getEnd_time();
        String texto = primeiroNomeSet + " - " + segundooNomeSet;
        primeiroNome.setText(texto);
        String[] arrOfStr = bookings.get(position).getDay().split(" ", 5);
        String mesa = arrOfStr[2];
        String dia = arrOfStr[1];
        String mes = "";
        String ano = arrOfStr[3];
        switch (mesa) {
            case "Jan":
                mes = "1";
                break;
            case "Feb":
                mes = "2";
                break;
            case "Mar":
                mes = "3";
                break;
            case "Apr":
                mes = "4";
                break;
            case "May":
                mes = "5";
                break;
            case "Jun":
                mes = "6";
                break;
            case "Jul":
                mes = "7";
                break;
            case "Ago":
                mes = "8";
                break;
            case "Sep":
                mes = "9";
                break;
            case "Oct":
                mes = "10";
                break;
            case "Nov":
                mes = "11";
                break;
            case "Dec":
                mes = "12";
                break;
        }
        String data = dia+"/"+mes+"/"+ano;
        email.setText(data);
        return rowView;
    }


}
