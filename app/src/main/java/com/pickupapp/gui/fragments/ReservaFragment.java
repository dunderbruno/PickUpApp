package com.pickupapp.gui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pickupapp.R;
import com.pickupapp.dominio.Booking;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservaFragment extends Fragment {
    protected static Booking booking;
    private TextView spotName, bookingTime;
    private Button avaliar;



    public ReservaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_reserva, container, false);
        spotName = inflate.findViewById(R.id.spot_nome_reserva);
        bookingTime = inflate.findViewById(R.id.spot_horario_reserva);
        avaliar = inflate.findViewById(R.id.button_avaliar);
        spotName.setText(booking.getSpot_name());
        String texto = booking.getStart_time()+" - "+booking.getEnd_time();
        bookingTime.setText(texto);
        avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return inflate;
    }

}
