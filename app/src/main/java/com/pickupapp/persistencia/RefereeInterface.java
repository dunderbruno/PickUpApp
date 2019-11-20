package com.pickupapp.persistencia;

import com.pickupapp.dominio.Person;
import com.pickupapp.dominio.Referee;
import com.pickupapp.persistencia.retorno.SetCall;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RefereeInterface {

    @POST("referee")
    Call<Referee> criarReferee(@Body Map<String, String> parameters);

    @POST("user/{id}/referee")
    Call<Referee> setRefereeUsuario(@Body Map<String,String> parameters, @Path("id") int id);

    @GET ("referee")
    Call<Person> getReferee();
}
