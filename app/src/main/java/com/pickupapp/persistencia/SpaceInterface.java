package com.pickupapp.persistencia;

import com.pickupapp.dominio.Space;
import com.pickupapp.persistencia.retorno.SetCall;
import com.pickupapp.persistencia.retorno.SpotCall;
import com.pickupapp.persistencia.retorno.SpotCallGet;
import com.pickupapp.persistencia.retorno.Spots;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpaceInterface {

    @GET("spot")
    Call<Spots> getSpaces(@Header("Authorization") String token, @Header("x-access-token") String token1);

    @POST("spot")
    Call<SpotCall> registerSpace(@Header("Authorization") String token,
                                 @Header("x-access-token") String token1,
                                 @Body Map<String,String> parameters);

    @GET("spot/my")
    Call<Spots> getMySpaces(@Header("Authorization") String token, @Header("x-access-token") String token1);

    @GET("/spot/{spot_id}")
    Call<Space> getSpace(@Header("x-access-token") String token1,
                               @Path("spot_id") String spot);

}
