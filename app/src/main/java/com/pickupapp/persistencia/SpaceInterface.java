package com.pickupapp.persistencia;

import com.pickupapp.dominio.Space;
import com.pickupapp.persistencia.retorno.Spots;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface SpaceInterface {

    @GET("spot")
    Call<Spots> getSpaces(@Header("Authorization") String token, @Header("x-access-token") String token1);
}
