package com.pickupapp.persistencia;

import com.pickupapp.dominio.User;
import com.pickupapp.persistencia.retorno.Token;
import com.pickupapp.persistencia.retorno.UserCall;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserInterface {

    @GET("login")
    Call<Token> login(@Header("Authorization") String token, @Header("x-access-token") String token1, @Query("user_group") String group);

    @GET("/user/username/{username}")
    Call<UserCall> buscarId(@Path("username") String username);

    @POST("signup")
    Call<User> register(@Body Map<String,String> parameters);

    @POST("user/{id}/group")
    Call<User> setGrupoUsuario(@Body Map<String,String> parameters, @Path("id") int id);
}
