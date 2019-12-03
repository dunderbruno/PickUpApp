package com.pickupapp.persistencia;


import com.pickupapp.persistencia.retorno.Invites;
import com.pickupapp.persistencia.retorno.SetCall;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface InviteInterface {

    @POST("/players/invites")
    Call<SetCall> registerInvite(@Header("x-access-token") String token1,
                                  @Body Map<String,String> parameters);

    @POST("/players/invites/accept")
    Call<SetCall> acceptInvite(@Header("x-access-token") String token1,
                                 @Body Map<String,String> parameters);

    @GET("/players/invites/guest")
    Call<Invites> getInvites(@Header("x-access-token") String token1);

}
