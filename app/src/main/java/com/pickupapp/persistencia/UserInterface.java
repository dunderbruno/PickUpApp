package com.pickupapp.persistencia;

import com.pickupapp.persistencia.retorno.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserInterface {

    @GET("login")
    Call<Token> login(@Header("Authorization") String token, @Header("x-access-token") String token1);
}
