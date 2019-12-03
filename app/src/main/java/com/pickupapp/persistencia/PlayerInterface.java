package com.pickupapp.persistencia;

import com.pickupapp.persistencia.retorno.Players;
import com.pickupapp.persistencia.retorno.idCall;

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

    @GET("players")
    Call<Players> getPlayers(@Header("x-access-token") String token1);

    @GET("/players/{user_id}")
    Call<idCall> getPlayerId(@Header("x-access-token") String token1, @Path("user_id") String userId);

    @GET("/booking/{booking_id}/players")
    Call<Players> getBookingPlayers(@Header("x-access-token") String token1,
                                    @Path("booking_id") String booking);

}
