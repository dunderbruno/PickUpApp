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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pickupapp.R;
import com.pickupapp.dominio.Booking;
import com.pickupapp.dominio.Photo;
import com.pickupapp.dominio.Space;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.PhotoInterface;
import com.pickupapp.persistencia.SpaceInterface;
import com.pickupapp.persistencia.retorno.SetCall;
import com.pickupapp.persistencia.retorno.SpotPhotosCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import afu.org.checkerframework.checker.oigj.qual.O;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservaFragment extends Fragment {
    protected static Booking booking;
    private TextView spotName, bookingTime;
    private EditText nota;
    private Button avaliar, local, salvar;
    private LinearLayout dadosnota, dados;



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
        dados = inflate.findViewById(R.id.dadosReserva);
        nota=inflate.findViewById(R.id.nota);
        dadosnota = inflate. findViewById(R.id.dadosnota);
        spotName.setText(booking.getSpot_name());
        String texto = booking.getStart_time()+" - "+booking.getEnd_time();
        bookingTime.setText(texto);
        if (Sessao.getSessao(getContext()).getGroup().getGroup_name().equals("2")){
            avaliar.setVisibility(View.GONE);
        }
        avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dados.setVisibility(View.INVISIBLE);
                dadosnota.setVisibility(View.VISIBLE);
            }
        });
        salvar = inflate.findViewById(R.id.buttonsalvarnota);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notat = nota.getText().toString();
                avaliarSpace(notat);
            }
        });
        local = inflate.findViewById(R.id.button_local);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpace();
            }
        });
        return inflate;
    }

    private void getSpace() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceInterface spaceInterface = retrofit.create(SpaceInterface.class);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<Space> call = spaceInterface.getSpace(token, String.valueOf(booking.getSpot_id()));
        call.enqueue(new Callback<Space>() {
            @Override
            public void onResponse(Call<Space> call, Response<Space> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response.message());
                    return;
                }
                Space spot = response.body();
                Fragment fragment = new SpaceFragment();
                SpaceFragment.spot = spot;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(getId(), fragment);
                transaction.commit();
            }

            @Override
            public void onFailure(Call<Space> call, Throwable t) {
            }
        });
    }

    private void avaliarSpace(String nota) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceInterface spaceInterface = retrofit.create(SpaceInterface.class);
        String token = Sessao.getSessao(getContext()).getToken();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("rating", nota);
        params.put("spot_id", booking.getSpot_id());
        Log.d("resposta", String.valueOf(booking.getSpot_id()));
        Call<SetCall> call = spaceInterface.rateSpace(token, params);
        call.enqueue(new Callback<SetCall>() {
            @Override
            public void onResponse(Call<SetCall> call, Response<SetCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response.message());
                    return;
                }
                Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_LONG);
                Fragment fragment = new ListBookingFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(getId(), fragment);
                transaction.commit();
            }

            @Override
            public void onFailure(Call<SetCall> call, Throwable t) {
            }
        });
    }

}
