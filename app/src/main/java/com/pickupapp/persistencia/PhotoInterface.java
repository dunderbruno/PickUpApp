package com.pickupapp.persistencia;

import com.pickupapp.persistencia.retorno.SetCall;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PhotoInterface {

    @POST("spot/photo")
    Call<SetCall> registrarPhoto(@Header("Authorization") String token,
                                 @Header("x-access-token") String token1,
                                 @Body Map<String,String> parameters);
}
