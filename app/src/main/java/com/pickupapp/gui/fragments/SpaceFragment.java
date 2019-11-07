package com.pickupapp.gui.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pickupapp.R;
import com.pickupapp.dominio.Schedule;
import com.pickupapp.dominio.Space;
import com.pickupapp.infra.Sessao;
import com.pickupapp.persistencia.PhotoInterface;
import com.pickupapp.persistencia.SpaceInterface;
import com.pickupapp.persistencia.retorno.SpotCall;
import com.pickupapp.persistencia.retorno.SpotCallGet;
import com.pickupapp.persistencia.retorno.SpotPhotosCall;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpaceFragment extends Fragment {
    private ImageView imageView;
    private FloatingActionButton reservarFloating;
    protected static Space spot;
    private Button reservar;
    private TextView nome;
    private TextView endereco;
    private TextView preco;
    private ProgressBar progressBar;
    private ImageView imagem1, imagem2, imagem3, imagem4, imagem5;
    private TextView funcionamentoSegunda,funcionamentoTerca, funcionamentoQuarta,funcionamentoQuinta,
                        funcionamentoSexta,funcionamentoSabado,funcionamentoDomingo;
    private int posicao = 0;

    private ArrayList<Bitmap> gallery = new ArrayList<>();

    private int position;
    private int posicaoIncercao = 0;

    private static final Integer DURATION = 2500;

    private Timer timer = null;

    public SpaceFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_space, container, false);
        nome = inflate.findViewById(R.id.name_space_perfil);
        endereco = inflate.findViewById(R.id.address_space_perfil);
        preco = inflate.findViewById(R.id.price_space_perfil);
        progressBar = inflate.findViewById(R.id.progressBarSpot);
        imageView = inflate.findViewById(R.id.imagensSpot);
        imagem1 = inflate.findViewById(R.id.imagensSpot1);
        imagem2 = inflate.findViewById(R.id.imagensSpot2);
        imagem3 = inflate.findViewById(R.id.imagensSpot3);
        imagem4 = inflate.findViewById(R.id.imagensSpot4);
        imagem5 = inflate.findViewById(R.id.imagensSpot5);
        funcionamentoSegunda = inflate.findViewById(R.id.funcionamentoSegunda);
        funcionamentoTerca = inflate.findViewById(R.id.funcionamentoTerca);
        funcionamentoQuarta = inflate.findViewById(R.id.funcionamentoQuarta);
        funcionamentoQuinta = inflate.findViewById(R.id.funcionamentoQuinta);
        funcionamentoSexta = inflate.findViewById(R.id.funcionamentoSexta);
        funcionamentoSabado = inflate.findViewById(R.id.funcionamentoSabado);
        funcionamentoDomingo = inflate.findViewById(R.id.funcionamentoDomingo);
        buscarSpot();
        getPhotos();
//        if (Sessao.getSessao(getContext()).getGroup().getGroup_name().equals("2")){
//            reservar.setVisibility(View.GONE);
//        }
        reservarFloating = inflate.findViewById(R.id.reservarFloating);
        reservarFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CalendarFragment();
                CalendarFragment.spotId = String.valueOf(spot.getId());
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(getId(), fragment);
                transaction.commit();
            }
        });
        reservar = inflate.findViewById(R.id.abrirCalendario);
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CalendarFragment();
                CalendarFragment.spotId = String.valueOf(spot.getId());
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(getId(), fragment);
                transaction.commit();
            }
        });
        return inflate;
    }

    private void getPhotos() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PhotoInterface photoInterface = retrofit.create(PhotoInterface.class);
        String credentials = Sessao.getSessao(getContext()).getUsername()+":"+Sessao.getSessao(getContext()).getPassword();
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<SpotPhotosCall> call = photoInterface.getSpotPhotos(auth, token, String.valueOf(spot.getId()));
        call.enqueue(new Callback<SpotPhotosCall>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SpotPhotosCall> call, Response<SpotPhotosCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response.message());
                    return;
                }
                SpotPhotosCall spotphotosCall = response.body();
                ArrayList photos = spotphotosCall.getPhotos();
                if (!photos.isEmpty()){
                    spotphotosCall.getPhotos().forEach((n) -> addPhoto(n.getImage()));
                }
            }

            @Override
            public void onFailure(Call<SpotPhotosCall> call, Throwable t) {
            }
        });
    }

    private void addPhoto(String image) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PhotoInterface photoInterface = retrofit.create(PhotoInterface.class);
        String credentials = Sessao.getSessao(getContext()).getUsername()+":"+Sessao.getSessao(getContext()).getPassword();
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<ResponseBody> call = photoInterface.getPhoto(auth,token,image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                        if (posicao == 0){
                            imageView.setBackground(new BitmapDrawable(bm));
                        }else if(posicao == 1){
                            imagem1.setVisibility(View.VISIBLE);
                            imagem1.setBackground(new BitmapDrawable(bm));
                        }else if(posicao == 2){
                            imagem2.setVisibility(View.VISIBLE);
                            imagem2.setBackground(new BitmapDrawable(bm));
                        }else if(posicao == 3){
                            imagem3.setVisibility(View.VISIBLE);
                            imagem3.setBackground(new BitmapDrawable(bm));
                        }else if(posicao == 4){
                            imagem4.setVisibility(View.VISIBLE);
                            imagem4.setBackground(new BitmapDrawable(bm));
                        }else if(posicao == 5){
                            imagem5.setVisibility(View.VISIBLE);
                            imagem5.setBackground(new BitmapDrawable(bm));
                        }
                        posicao+=1;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void buscarSpot() {
        nome.setText(spot.getName());
        endereco.setText(spot.getAddress().toString());
        String precot = "R$"+spot.getPriceHour().toString();
        preco.setText(precot);
        if (spot.getSchedule() != null){
            ArrayList<Schedule> schedules = spot.getSchedule();
            schedules.forEach((n)->showSchedule(n));
        }
    }

    private void showSchedule(Schedule n){
        if(n.getWeek_day() == 2){
            funcionamentoSegunda.setVisibility(View.VISIBLE);
            String text = "Segunda: "+n.getOpening_time()+"-"+n.getClosing_time();
            funcionamentoSegunda.setText(text);
        }else if(n.getWeek_day() == 3){
            funcionamentoTerca.setVisibility(View.VISIBLE);
            String text = "Terça:       "+n.getOpening_time()+"-"+n.getClosing_time();
            funcionamentoTerca.setText(text);
        }else if(n.getWeek_day() == 4){
            funcionamentoQuarta.setVisibility(View.VISIBLE);
            String text = "Quarta:     "+n.getOpening_time()+"-"+n.getClosing_time();
            funcionamentoQuarta.setText(text);
        }else if(n.getWeek_day() == 5){
            funcionamentoQuinta.setVisibility(View.VISIBLE);
            String text = "Quinta:      "+n.getOpening_time()+"-"+n.getClosing_time();
            funcionamentoQuinta.setText(text);
        }else if(n.getWeek_day() == 6){
            funcionamentoSexta.setVisibility(View.VISIBLE);
            String text = "Sexta:        "+n.getOpening_time()+"-"+n.getClosing_time();
            funcionamentoSexta.setText(text);
        }else if(n.getWeek_day() == 7){
            funcionamentoSabado.setVisibility(View.VISIBLE);
            String text = "Sabado:    "+n.getOpening_time()+"-"+n.getClosing_time();
            funcionamentoSabado.setText(text);
        }else if(n.getWeek_day() == 1){
            funcionamentoDomingo.setVisibility(View.VISIBLE);
            String text = "Domingo: "+n.getOpening_time()+"-"+n.getClosing_time();
            funcionamentoDomingo.setText(text);
        }
    }
}
