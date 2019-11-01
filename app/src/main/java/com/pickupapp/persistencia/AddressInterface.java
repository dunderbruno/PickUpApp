package com.pickupapp.persistencia;


import com.pickupapp.persistencia.retorno.AddressCall;
import com.pickupapp.persistencia.retorno.CitysCall;
import com.pickupapp.persistencia.retorno.StatesCall;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AddressInterface {

    @GET("state/{state_id}/city")
    Call<CitysCall> getAllCity(@Header("Authorization") String token,
                               @Header("x-access-token") String token1,
                               @Path("state_id") String state);

    @GET("state")
    Call<StatesCall> getAllStates(@Header("Authorization") String token,
                                     @Header("x-access-token") String token1);

}
