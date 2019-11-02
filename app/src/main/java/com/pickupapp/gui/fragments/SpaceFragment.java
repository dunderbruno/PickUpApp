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

import com.pickupapp.R;
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
    protected static String spotId;
    private Button reservar;
    private TextView nome;
    private TextView endereco;
    private TextView preco;
    private ProgressBar progressBar;

    private Bitmap[] gallery = { };

    private int position;
    private int posicaoIncercao = 0;

    private static final Integer DURATION = 2500;

    private Timer timer = null;

    public SpaceFragment() {
        // Required empty public constructor
    }


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
        buscarSpot();
        getPhotos();
//        if (Sessao.getSessao(getContext()).getGroup().getGroup_name().equals("2")){
//            reservar.setVisibility(View.GONE);
//        }
        reservar = inflate.findViewById(R.id.abrirCalendario);
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CalendarFragment();
                CalendarFragment.spotId = spotId;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(getId(), fragment);
                transaction.commit();
            }
        });
        return inflate;
    }

    private void getPhotos() {
        progressBar.setVisibility(View.VISIBLE);
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
        Call<SpotPhotosCall> call = photoInterface.getSpotPhotos(auth, token, spotId);
        call.enqueue(new Callback<SpotPhotosCall>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SpotPhotosCall> call, Response<SpotPhotosCall> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response.message());
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                SpotPhotosCall spotphotosCall = response.body();
                ArrayList photos = spotphotosCall.getPhotos();
                if (!photos.isEmpty()){
                    spotphotosCall.getPhotos().forEach((n) -> addPhoto(n.getImage()));
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<SpotPhotosCall> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
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
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                        imageView.setImageBitmap(bm);
                        gallery[posicaoIncercao] = bm;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        posicaoIncercao += 1;
    }

    private void buscarSpot() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pickupbsiapi.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SpaceInterface spotInterface = retrofit.create(SpaceInterface.class);
        String credentials = Sessao.getSessao(getContext()).getUsername()+":"+Sessao.getSessao(getContext()).getPassword();
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        String token = Sessao.getSessao(getContext()).getToken();
        Call<SpotCallGet> call = spotInterface.getSpace(auth, Sessao.getSessao(getContext()).getToken(), spotId);
        call.enqueue(new Callback<SpotCallGet>() {
            @Override
            public void onResponse(Call<SpotCallGet> call, Response<SpotCallGet> response) {
                if (!response.isSuccessful()){
                    Log.d("resposta", "onResponse: "+response.message());
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                SpotCallGet spotCall = response.body();
                nome.setText(spotCall.getName());
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<SpotCallGet> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
