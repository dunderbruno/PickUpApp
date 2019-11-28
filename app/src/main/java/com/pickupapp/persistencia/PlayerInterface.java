package com.pickupapp.persistencia;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PlayerInterface {

    @GET("spot")
    Call<> getSpaces(@Header("Authorization") String token, @Header("x-access-token") String token1);

}
