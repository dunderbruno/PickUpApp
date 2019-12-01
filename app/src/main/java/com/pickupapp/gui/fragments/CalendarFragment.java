package com.pickupapp.gui.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.pickupapp.R;
import com.pickupapp.dominio.Booking;
import com.pickupapp.infra.Mask;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.BookingInterface;
import com.pickupapp.persistencia.SpaceInterface;
import com.pickupapp.persistencia.retorno.BookingsCall;
import com.pickupapp.persistencia.retorno.SetCall;
import com.pickupapp.persistencia.retorno.Spots;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    protected static String spotId = "";
    private Button reservar;
    private EditText horaInicial, horaFinal;
    private DateData lastDate;
    private ArrayList<Booking> bookings = null;
    private ProgressBar progressBar;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_calendar, container, false);
        final MCalendarView cv = inflate.findViewById(R.id.mcalendarView);
        reservar = inflate.findViewById(R.id.reservar);
        horaFinal = inflate.findViewById(R.id.horaFinal);
        horaInicial = inflate.findViewById(R.id.horaInicial);
        progressBar = inflate.findViewById(R.id.progressBar);
        horaInicial.addTextChangedListener(Mask.insert(Mask.HORA, horaInicial));
        horaFinal.addTextChangedListener(Mask.insert(Mask.HORA, horaFinal));
        if(Sessao.getSessao(getContext()).getGroup().getGroup_name().equals("2")){
            reservar.setVisibility(View.GONE);
            horaFinal.setVisibility(View.GONE);
            horaInicial.setVisibility(View.GONE);
        }
        cv.getMarkedDates().getAll().clear();
        final int[] count = {0};
        cv.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                if (count[0] != 0){
                    cv.unMarkDate(lastDate);
                }
                count[0] = 1;
                cv.markDate(date);
                lastDate = date;
                boolean show = false;
                StringBuilder horarios = new StringBuilder();
                for (Booking n : bookings) {
                    String[] arrOfStr = n.getDay().split(" ", 5);
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
                    if(dia.equals(String.valueOf(date.getDay()))&mes.equals(String.valueOf(date.getMonth()))&ano.equals(String.valueOf(date.getYear()))) {
                        show = true;
                        horarios.append(n.getStart_time()).append(" - ").append(n.getEnd_time()).append("\n");
                    }
                }
                if (show) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Reservas desse dia")
                            .setMessage(horarios.toString())
                            .setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }

                Log.d("resposta", String.valueOf(date.getYear()));
                Log.d("resposta", String.valueOf(date.getDay()));
                Log.d("resposta", String.valueOf(date.getMonth()));
            }
        });
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservarHorario();
            }
        });
        searchSpotDates(cv);
        return inflate;
    }

    private void reservarHorario() {
        progressBar.setVisibility(View.VISIBLE);
        String day = String.valueOf(lastDate.getYear()) + "-" + lastDate.getMonthString() + "-" + lastDate.getDayString();
        if (validarReserva(day, horaInicial.getText().toString(), horaFinal.getText().toString())) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://pickupbsiapi.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            BookingInterface bookingInterface = retrofit.create(BookingInterface.class);
            String credentials = Sessao.getSessao(getContext()).getUsername() + ":" + Sessao.getSessao(getContext()).getPassword();
            String auth = "Basic "
                    + Base64.encodeToString(credentials.getBytes(),
                    Base64.NO_WRAP);
            String token = Sessao.getSessao(getContext()).getToken();
            final Map<String, String> params = new HashMap<String, String>();
            params.put("spot_id", spotId);
            params.put("day", day);
            params.put("start_time", horaInicial.getText().toString());
            params.put("end_time", horaFinal.getText().toString());
            Call<SetCall> call = bookingInterface.registerBooking(auth, Sessao.getSessao(getContext()).getToken(), params);
            call.enqueue(new Callback<SetCall>() {
                @Override
                public void onResponse(Call<SetCall> call, Response<SetCall> response) {
                    if (!response.isSuccessful()) {
                        Log.d("resposta", "onResponse: " + response);
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Reservado com sucesso!", Toast.LENGTH_LONG).show();
                    Fragment fragment = new WelcomeFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(getId(), fragment);
                    transaction.commit();
                }

                @Override
                public void onFailure(Call<SetCall> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private boolean validarReserva(String day,String start, String finall) {
        for (Booking n : bookings) {
            String[] arrOfStr = n.getDay().split(" ", 5);
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
            String[] date2 = day.split("-",5);
            if(dia.equals(date2[2])&mes.equals(date2[1])&ano.equals(date2[0])) {
                String[] tempo1 = n.getStart_time().split(":");
                String[] tempofinal1 = n.getEnd_time().split(":");

                String[] tempo2 = start.split(":");
                String[] tempofinal = finall.split(":");
                if ((Integer.valueOf(tempo1[0]) <= Integer.valueOf(tempo2[0]))
                        && (Integer.valueOf(tempo1[0]) <= Integer.valueOf(tempofinal[0]))
                        &&(Integer.valueOf(tempofinal1[0]) >= Integer.valueOf(tempo2[0]))
                        && (Integer.valueOf(tempofinal1[0]) >= Integer.valueOf(tempofinal[0]))) {
                    return false;
                }
            }
        }
        return true;
    }

    private void searchSpotDates(MCalendarView cv) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookingInterface bookingInterface = retrofit.create(BookingInterface.class);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<BookingsCall> call = bookingInterface.getBooking(token, spotId);
        call.enqueue(new Callback<BookingsCall>() {
            @Override
            public void onResponse(Call<BookingsCall> call, Response<BookingsCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                BookingsCall bookingsCall = response.body();
                bookings = bookingsCall.getBookings();
                if (!bookings.isEmpty()){
                    for (Booking n : bookings) {
                        marcarCalendario(cv, n);
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<BookingsCall> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void marcarCalendario(MCalendarView cv, Booking n) {
        String[] arrOfStr = n.getDay().split(" ", 5);
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
        DateData date = new DateData(Integer.parseInt(ano),Integer.parseInt(mes),Integer.parseInt(dia));
        cv.markDate(date);
    }


}
